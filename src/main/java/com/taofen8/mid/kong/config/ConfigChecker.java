/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.config;

import com.taofen8.mid.kong.config.Config.ConfigEntry;
import com.taofen8.mid.kong.config.internal.InternalConfig.InternalConfigEntry;
import com.taofen8.mid.kong.dispatch.exception.KongConfigException;
import com.taofen8.mid.kong.register.support.EnvHelper;
import org.springframework.util.StringUtils;

public class ConfigChecker {

  private ConfigChecker() {
  }

  public static void check(Config config) throws KongConfigException {
    if (config == null) {
      throw new KongConfigException("kong config bean has been not sepecified ");
    }
    if (StringUtils.isEmpty(config.getConfig(ConfigEntry.KONG_SERVER_ADMIN_URL))) {
      throw new KongConfigException(
          "kong config [kong.config.server.admin.url] has  been not sepecified ");
    }
    if (StringUtils.isEmpty(config.getConfig(ConfigEntry.KONG_SERVER_APP_IDENTIFIER))) {
      throw new KongConfigException(
          "kong config [kong.config.server.app.identifier] has been not sepecified ");
    }

    boolean autoResolve = ConfigResolver
        .isWitchOn(config.getConfig(ConfigEntry.KONG_SERVER_ADDRESS_AUTO_RESOLVE));

    String ip = EnvHelper.getLocalIP(autoResolve);
    if (StringUtils.isEmpty(ip)) {
      throw new KongConfigException("can not find a valid ip , please check your env setting of HOST_IP");
    }
    int port = EnvHelper.getTomcatPort(autoResolve);

    ConfigProvider.getInternalConfig().configure(InternalConfigEntry.IP, ip);
    ConfigProvider.getInternalConfig().configure(InternalConfigEntry.PORT, String.valueOf(port));
  }
}
