/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.register.internal;


import com.taofen8.mid.kong.annotation.KongServiceMapping;
import com.taofen8.mid.kong.config.Config;
import com.taofen8.mid.kong.config.Config.ConfigEntry;
import com.taofen8.mid.kong.config.ConfigProvider;
import com.taofen8.mid.kong.config.internal.InternalConfig.InternalConfigEntry;
import com.taofen8.mid.kong.register.AbstractServiceRegistration;
import com.taofen8.mid.kong.register.struct.Invocation.Builder;
import com.taofen8.mid.kong.register.struct.MicroService;
import com.taofen8.mid.kong.register.struct.Route;
import com.taofen8.mid.kong.register.struct.Signature;
import com.taofen8.mid.kong.register.struct.Target;
import com.taofen8.mid.kong.register.struct.Upstream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

public class KongMappingRegistration extends AbstractServiceRegistration<MicroService> {

  Logger logger = LoggerFactory.getLogger(KongMappingRegistration.class);


  @Override
  public Map<String, MicroService> collectServices(ApplicationContext context,
      Config config) {
    Map<String, MicroService> serviceMap = new HashMap<String, MicroService>();
    collectServicesFromContext(context, config, serviceMap);
    if (context.getParent() != null) {
      collectServicesFromContext(context.getParent(), config, serviceMap);
    }
    return serviceMap;
  }

  private void collectServicesFromContext(ApplicationContext context, Config config,
      Map<String, MicroService> serviceMap) {

    Target target = new Target.Builder().target(
        new StringBuffer(ConfigProvider.getInternalConfig().getConfig(InternalConfigEntry.IP))
            .append(":")
            .append(ConfigProvider.getInternalConfig().getConfig(InternalConfigEntry.PORT))
            .toString())
        .build();

    String upstreamName = config.getConfig(ConfigEntry.KONG_SERVER_APP_IDENTIFIER);
    Upstream upstream = new Upstream.Builder()
        .target(target)
        .name(upstreamName)
        .healthcheckConfig(config.getConfig(ConfigEntry.KONG_SERVER_HEALTHCHECKS))
        .build();

    String[] allbBeanNames = context.getBeanDefinitionNames();
    if (allbBeanNames != null && allbBeanNames.length > 0) {
      for (String beanName : allbBeanNames) {
        Object bean = null;
        try {
          bean = context.getBean(beanName);
        } catch (BeansException e) {
          logger.warn(e.getMessage());
          continue;
        }

        if (bean != null) {
          Method[] methods = bean.getClass().getDeclaredMethods();
          for (Method method : methods) {
            if (method.getParameterTypes().length != 1) {
              continue;
            }
            KongServiceMapping kongServiceMapping = method.getAnnotation(KongServiceMapping.class);
            if (kongServiceMapping == null) {
              continue;
            }

            String[] hosts =
                StringUtils.isEmpty(config.getConfig(ConfigEntry.KONG_SERVER_ROUTE_HOSTS))
                    ? new String[]{}
                    : StringUtils
                        .trimAllWhitespace(config.getConfig(ConfigEntry.KONG_SERVER_ROUTE_HOSTS))
                        .split(",");

            if (kongServiceMapping.hosts().length > 0) {
              hosts = kongServiceMapping.hosts();
            }

            Route route = new Route.Builder().methods(Arrays.asList(kongServiceMapping.methods()))
                .hosts(Arrays.asList(hosts))
                .path(kongServiceMapping.path()).preserveHost(true).build();
            String localName = this.replaceSlash(kongServiceMapping.path());

            MicroService service = new MicroService.Builder()
                .name(localName)
                .upstream(upstream)
                .route(route)
                .host(upstreamName)
                .serviceMapping(kongServiceMapping)
                .invocation(
                    new Builder()
                        .bean(bean)
                        .signature(
                            new Signature.Builder()
                                .methodName(method.getName())
                                .requestType(method.getParameterTypes()[0])
                                .responseType(method.getReturnType())
                                .build()
                        )
                        .build()
                )
                .build();
            serviceMap.put(localName, service);
          }
        }
      }
    }
  }

  @Override
  protected String getServiceKey(HttpServletRequest request) {
    return this.replaceSlash(request.getRequestURI());
  }

  private String replaceSlash(String path) {
    if (StringUtils.isEmpty(path)) {
      return null;
    }
    path = path.replaceAll("/+", ".");
    if (path.startsWith(".")) {
      path = path.substring(1);
    }
    return path;
  }

}
