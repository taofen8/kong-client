/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.config;

import com.taofen8.mid.kong.dispatch.exception.KongConfigException;
import com.taofen8.mid.kong.config.Config.ConfigEntry;
import org.springframework.util.StringUtils;

public class ConfigChecker {

  private ConfigChecker(){}

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
  }
}
