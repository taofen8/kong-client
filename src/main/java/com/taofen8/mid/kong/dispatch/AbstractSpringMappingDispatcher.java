/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.dispatch;

import com.taofen8.mid.kong.register.ServiceRegistration;
import com.taofen8.mid.kong.config.Config;
import com.taofen8.mid.kong.register.internal.SpringMappingRegistration;
import com.taofen8.mid.kong.register.struct.Service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;

public abstract class AbstractSpringMappingDispatcher extends AbstractKongDispatcher {

  private ServiceRegistration serviceRegistry = new SpringMappingRegistration();

  @Override
  public ServiceRegistration getServiceRegistration() {
    return serviceRegistry;
  }

  @Override
  public void init(ApplicationContext context, Config config) {

  }

  @Override
  public boolean kongDispatch(Service service, HttpServletRequest request,
      HttpServletResponse response) {
    return false;
  }
}
