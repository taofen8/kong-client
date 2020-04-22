/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.builtin;

import com.taofen8.mid.kong.dispatch.AbstractKongDispatcher;
import com.taofen8.mid.kong.dispatch.exception.KongConfigException;
import com.taofen8.mid.kong.register.ServiceRegistration;
import com.taofen8.mid.kong.builtin.meta.VersionInfo;
import com.taofen8.mid.kong.config.Config;
import com.taofen8.mid.kong.register.struct.Service;
import com.taofen8.mid.kong.util.JsonUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;

public class BuiltinServiceDispatcher extends AbstractKongDispatcher {

  private BuiltinServiceRegistration serviceRegistry = new BuiltinServiceRegistration();

  @Override
  protected void init(ApplicationContext context, Config config) throws KongConfigException {
    VersionInfo.loadVersion();
  }

  @Override
  public boolean kongDispatch(Service service, HttpServletRequest request,
      HttpServletResponse response) {

    BuiltinServiceEntity serviceLocal = (BuiltinServiceEntity) service;

    int status = 200;
    try {
      Object invResponse = serviceLocal.invocation().invoke(request.getParameterMap());
      response.getOutputStream().write(JsonUtil.toJSONString(invResponse).getBytes());
    } catch (Exception e) {
      status = 500;
    }
    response.setStatus(status);
    return true;
  }

  @Override
  public ServiceRegistration getServiceRegistration() {
    return this.serviceRegistry;
  }
}
