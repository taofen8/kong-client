/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong;

import com.taofen8.mid.kong.builtin.BuiltinServiceDispatcher;
import com.taofen8.mid.kong.builtin.monitor.ServiceMonitor;
import com.taofen8.mid.kong.config.ConfigChecker;
import com.taofen8.mid.kong.config.ConfigLoader;
import com.taofen8.mid.kong.dispatch.KongDispatcher;
import com.taofen8.mid.kong.dispatch.exception.KongConfigException;
import com.taofen8.mid.kong.dispatch.support.EmbeddedDispatcherFactory;
import com.taofen8.mid.kong.register.executor.ServiceCommiter;
import com.taofen8.mid.kong.spring.BeanFactory;
import com.taofen8.mid.kong.config.Config;
import com.taofen8.mid.kong.config.Config.ConfigEntry;
import com.taofen8.mid.kong.register.struct.Service;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class KongDispatcherServlet extends DispatcherServlet {

  protected static Logger log = LoggerFactory.getLogger(KongDispatcherServlet.class);

  private final List<KongDispatcher> dispatchers = new ArrayList<KongDispatcher>(4);
  private final ServiceMonitor monitor = ServiceMonitor.getMonitorInstance();

  @Override
  protected void initStrategies(ApplicationContext context) {
    super.initStrategies(context);//加载配置

    monitor.start();

    //load and check necessary config
    ConfigLoader configLoader = BeanFactory
        .getBeanForTypeIncludingAncestors(context, ConfigLoader.class);
    if (configLoader == null) {
      throw new IllegalArgumentException("kong ConfigLoader bean has been not sepecified ");
    }

    Config serverConfig = configLoader.buildConfig();

    try {
      ConfigChecker.check(serverConfig);
    } catch (KongConfigException e) {
      throw new IllegalArgumentException(e.getMessage());
    }

    // add builtin dispatcher
    dispatchers.add(new BuiltinServiceDispatcher());

    //lookup all dispatchers for startup, use default  dispatcher if no none specified
    String[] dispatcherNames = BeanFactoryUtils
        .beanNamesForTypeIncludingAncestors(context, KongDispatcher.class);
    if (dispatcherNames != null && dispatcherNames.length > 0) {
      for (String name : dispatcherNames) {
        KongDispatcher dispatcher = (KongDispatcher) context.getBean(name);
        dispatchers.add(dispatcher);
      }
    } else {
      try {
        KongDispatcher defaultDispatcher = EmbeddedDispatcherFactory
            .newEmbeddedDispatcher(
                serverConfig.getConfig(ConfigEntry.KONG_SERVER_PROXY_STRATEGY));
        dispatchers.add(defaultDispatcher);
      } catch (KongConfigException e) {
        throw new IllegalArgumentException(e.getMessage());
      }
    }

    // startup kongDispatcher and collect all services for register
    List<Service> allServices = new ArrayList<>();
    for (KongDispatcher patcher : dispatchers) {
      try {
        patcher.onStartUp(context, serverConfig);
        allServices.addAll(patcher.getServiceRegistration().getAllServices());
      } catch (KongConfigException e) {
        throw new IllegalArgumentException(e.getMessage());
      }
    }

    // check uniqueness of service name and then register them to kong server
    checkServices(allServices);
    new ServiceCommiter(allServices, serverConfig).commit();
  }

  private void checkServices(List<Service> allServices) {
    Set<String> namesCheckSet = new HashSet<>(allServices.size());
    for (Service service : allServices) {
      if (namesCheckSet.contains(service.name())) {
        throw new IllegalArgumentException("service name duplicate:" + service.name());
      } else {
        namesCheckSet.add(service.name());
        if (log.isInfoEnabled()) {
          log.info("service name :{}", service.name());
        }
      }
    }
    if (log.isInfoEnabled()) {
      log.info("services total:{}",allServices.size());
    }
  }

  /**
   * Override doDispatch, Kong gateway service will be first responder. If kong gateway cannot
   * answer, it will continue forward to normal http servlet.
   */
  @Override
  protected void doDispatch(HttpServletRequest request, HttpServletResponse response)
      throws Exception {

    monitor.incrRequestCount();

    Service service = null;
    for (KongDispatcher dispatcher : dispatchers) {
      service = dispatcher.getServiceRegistration().findService(request);
      if (service != null && dispatcher.kongDispatch(service, request, response)) {
        return;
      }
    }

    if (service == null) {
      log.warn("specified service not found for requestURI: {}", request.getRequestURI());
    }
    super.doDispatch(request, response);


  }
}
