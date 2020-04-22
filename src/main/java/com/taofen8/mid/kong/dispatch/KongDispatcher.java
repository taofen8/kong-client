/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.dispatch;

import com.taofen8.mid.kong.dispatch.exception.KongConfigException;
import com.taofen8.mid.kong.register.ServiceRegistration;
import com.taofen8.mid.kong.config.Config;
import com.taofen8.mid.kong.register.struct.Service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;

public interface KongDispatcher {

  /**
   * 初始化
   */
  void onStartUp(ApplicationContext context, Config config) throws KongConfigException;

  /**
   * 尝试通过kong处理，未能处理返回false
   *
   * @param request
   * @param response
   * @return
   */
  boolean kongDispatch(Service service, HttpServletRequest request, HttpServletResponse response);


  ServiceRegistration getServiceRegistration();

}
