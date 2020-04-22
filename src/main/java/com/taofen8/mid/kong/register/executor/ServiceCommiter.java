/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.register.executor;

import com.taofen8.mid.kong.config.Config;
import com.taofen8.mid.kong.config.Config.ConfigEntry;
import com.taofen8.mid.kong.register.struct.Service;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceCommiter {

  Logger logger = LoggerFactory.getLogger(ServiceCommiter.class);
  private List<Service> services;
  private Config config;

  public ServiceCommiter(List<Service> services,
      Config config) {
    this.services = services;
    this.config = config;
  }


  public void commit() {
    if (services != null) {
      new Thread(new Runnable() {
        @Override
        public void run() {
          logger.info("kong service register start...");
          KongExecutor kongExecutor = new KongExecutor(5, 10, 60, TimeUnit.SECONDS);
          kongExecutor.setAdminUrl(config.getConfig(ConfigEntry.KONG_SERVER_ADMIN_URL));
          kongExecutor.submit(services);
          if (kongExecutor.isCompleted()) {
            logger.info("kong service register finished.");
            kongExecutor.shutdown();
          }
        }
      }).start();
    } else {
      logger.warn("no services providered");
    }
  }
}
