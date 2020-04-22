/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.builtin;

import com.taofen8.mid.kong.register.AbstractServiceRegistration;
import com.taofen8.mid.kong.register.struct.Invocation.Builder;
import com.taofen8.mid.kong.register.struct.Route;
import com.taofen8.mid.kong.register.struct.Signature;
import com.taofen8.mid.kong.register.support.EnvHelper;
import com.taofen8.mid.kong.config.Config;
import com.taofen8.mid.kong.config.Config.ConfigEntry;
import com.taofen8.mid.kong.register.struct.Service;
import com.taofen8.mid.kong.register.struct.Target;
import com.taofen8.mid.kong.register.struct.Upstream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

public class BuiltinServiceRegistration extends AbstractServiceRegistration {

  @Override
  protected Map<String, Service> collectServices(ApplicationContext context, Config config) {

    Target target = new Target.Builder().target(
        new StringBuffer(EnvHelper.getLocalIP()).append(":").append(EnvHelper.getTomcatPort())
            .toString())
        .build();

    String upstreamName = "ask.builtin";
    Upstream upstream = new Upstream.Builder()
        .target(target)
        .name(upstreamName)
        .healthcheckConfig(config.getConfig(ConfigEntry.KONG_SERVER_HEALTHCHECKS))
        .build();

    Map<String, Service> serviceMap = new HashMap<String, Service>();
    BuiltinController builtinService = new BuiltinController();
    Method[] methods = builtinService.getClass().getDeclaredMethods();
    for (Method method : methods) {
      if (method.getParameterTypes().length != 1) {
        continue;
      }
      BuiltinServiceMapping builtinServiceMapping = method
          .getAnnotation(BuiltinServiceMapping.class);
      if (builtinServiceMapping == null) {
        continue;
      }

      String localName = this.replaceSlash(builtinServiceMapping.path());

      Route route = new Route.Builder().methods(Arrays.asList(builtinServiceMapping.methods()))
          .path(builtinServiceMapping.path()).preserveHost(true).build();

      Service service = new BuiltinServiceEntity.Builder()
          .name(localName)
          .upstream(upstream)
          .route(route)
          .host(upstreamName)
          .serviceMapping(builtinServiceMapping)
          .invocation(
              new Builder()
                  .bean(builtinService)
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
    return serviceMap;
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

  @Override
  protected String getServiceKey(HttpServletRequest request) {
    return replaceSlash(request.getRequestURI());
  }
}
