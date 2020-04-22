/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.dispatch;

import com.taofen8.mid.kong.register.ServiceRegistration;
import com.taofen8.mid.kong.config.Config;
import com.taofen8.mid.kong.dispatch.exception.KongConfigException;
import org.springframework.context.ApplicationContext;

public abstract class AbstractKongDispatcher implements KongDispatcher {

  protected abstract void init(ApplicationContext context, Config config)
      throws KongConfigException;


  @Override
  public void onStartUp(ApplicationContext context, Config config)
      throws KongConfigException {

    ServiceRegistration serviceRegistry = getServiceRegistration();
    if (serviceRegistry == null) {
      throw new KongConfigException("serviceRegistry of dispathcher [" + this.getClass().getName()
          + "] has not  been specified");
    }
    //注册服务
    serviceRegistry.registerServices(context, config);
    //其他初始化工作
    init(context, config);

  }
}
