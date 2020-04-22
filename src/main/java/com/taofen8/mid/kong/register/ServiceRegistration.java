/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.register;

import com.taofen8.mid.kong.config.Config;
import com.taofen8.mid.kong.register.struct.Service;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationContext;

public interface ServiceRegistration<T extends Service> {

  /**
   * service  register
   *  @param context
   * @param config*/
  void registerServices(ApplicationContext context, Config config);

  /**
   * find service
   *
   * @param request
   * @return
   */
  T findService(HttpServletRequest request);

  List<T> getAllServices();
}
