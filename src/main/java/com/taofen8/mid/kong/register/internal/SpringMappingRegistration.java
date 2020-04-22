/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.register.internal;

import com.taofen8.mid.kong.register.AbstractServiceRegistration;
import com.taofen8.mid.kong.register.struct.PathMapping;
import com.taofen8.mid.kong.register.struct.RequestMethod;
import com.taofen8.mid.kong.register.struct.Route;
import com.taofen8.mid.kong.register.struct.Service;
import com.taofen8.mid.kong.register.struct.Target;
import com.taofen8.mid.kong.register.struct.Upstream;
import com.taofen8.mid.kong.register.support.EnvHelper;
import com.taofen8.mid.kong.config.Config;
import com.taofen8.mid.kong.config.Config.ConfigEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public class SpringMappingRegistration extends AbstractServiceRegistration<Service> {

  Logger logger = LoggerFactory.getLogger(KongMappingRegistration.class);

  private static List<String> blackList = new ArrayList<>();
  private Map<String, PathMapping> pathMappings = new HashMap<>();
  private AntPathMatcher pathMatcher = new AntPathMatcher();

  static {
    blackList.add("org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController");
  }

  @Override
  protected Map<String, Service> collectServices(ApplicationContext context, Config config) {

    String upstreamName = config.getConfig(ConfigEntry.KONG_SERVER_APP_IDENTIFIER);
    String[] hosts =
        StringUtils.isEmpty(config.getConfig(ConfigEntry.KONG_SERVER_ROUTE_HOSTS))
            ? new String[]{}
            : StringUtils.trimAllWhitespace(config.getConfig(ConfigEntry.KONG_SERVER_ROUTE_HOSTS))
                .split(",");

    Target target = new Target.Builder().target(
        new StringBuffer(EnvHelper.getLocalIP()).append(":").append(EnvHelper.getTomcatPort())
            .toString())
        .build();

    Upstream upstream = new Upstream.Builder()
        .target(target)
        .name(upstreamName)
        .healthcheckConfig(config.getConfig(ConfigEntry.KONG_SERVER_HEALTHCHECKS))
        .build();

    Set<String> filters = new HashSet<>();
    Map<String, Service> serviceMap = new HashMap<>();
    RequestMappingHandlerMapping bean = context.getBean(RequestMappingHandlerMapping.class);
    Map<RequestMappingInfo, HandlerMethod> handlerMethods = bean.getHandlerMethods();
    for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
      HandlerMethod method = entry.getValue();
      if (method != null && blackList.contains(method.getBeanType().getName())) {
        continue;
      }
      RequestMappingInfo rmi = entry.getKey();
      PatternsRequestCondition prc = rmi.getPatternsCondition();
      if (prc != null && !CollectionUtils.isEmpty(prc.getPatterns())) {
        for (String path : prc.getPatterns()) {
          if (filters.contains(path)) {
            throw new IllegalArgumentException(
                "SpringMappingRegistry collectServices error,path[" + path + "] duplicate");
          }
          pathMappings.put(path,
              new PathMapping(new PathMapping.Builder().path(path)
                  .upstreamName(upstreamName).requestMappingInfo(rmi)));
        }
      }
    }

    for (PathMapping pathMapping : pathMappings.values()) {
      Set<org.springframework.web.bind.annotation.RequestMethod> methodset = pathMapping
          .getRequestMappingInfo().getMethodsCondition()
          .getMethods();

      List<RequestMethod> methods = new ArrayList<>(4);
      for (org.springframework.web.bind.annotation.RequestMethod method : methodset) {
        methods.add(RequestMethod.valueOf(method.name()));
      }

      if (CollectionUtils.isEmpty(methods)) {
        methods.add(RequestMethod.GET);
        methods.add(RequestMethod.POST);
      }

      Route route = new Route.Builder().methods(methods).hosts(Arrays.asList(hosts))
          .path(pathMapping.getKongPath()).preserveHost(true).build();

      Service service = new Service.Builder()
          .name(pathMapping.getLocalName())
          .upstream(upstream)
          .route(route)
          .host(upstreamName)
          .build();
      serviceMap.put(pathMapping.getLocalName(), service);
    }
    return serviceMap;
  }

  @Override
  protected String getServiceKey(HttpServletRequest request) {
    PathMapping mapping = getMaping(request.getRequestURI(), request);
    if (mapping != null) {
      return mapping.getLocalName();
    }
    return null;
  }


  private PathMapping getMaping(String uri, HttpServletRequest request) {
    if (pathMappings.containsKey(uri)) {
      return pathMappings.get(uri);
    }
    List<String> matched = new ArrayList<>(4);
    Iterator<PathMapping> iterator = pathMappings.values().iterator();

    while (iterator.hasNext()) {
      PathMapping mapping = iterator.next();
      matched.addAll(
          mapping.getRequestMappingInfo().getPatternsCondition().getMatchingCondition(request)
              .getPatterns());
    }
    if (CollectionUtils.isEmpty(matched)) {
      return null;
    }

    if (matched.size() > 1) {
      Collections.sort(matched, this.pathMatcher.getPatternComparator(uri));
    }
    return pathMappings.get(matched.get(0));
  }
}
