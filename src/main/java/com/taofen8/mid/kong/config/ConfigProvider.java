/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.config;

import com.taofen8.mid.kong.config.internal.InternalConfig;

public abstract class ConfigProvider {

  private ConfigProvider(){}

  private static final InternalConfig internalConfig = new InternalConfig();
  private static final Config config = new Config();

  public static Config getConfig() {
    return config;
  }

  public static InternalConfig getInternalConfig() {
    return internalConfig;
  }


}
