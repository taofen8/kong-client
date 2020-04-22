/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.config;

public abstract class ConfigResolver {

  private static String SWITCH_ON = "on";

  private ConfigResolver() {
  }

  public static boolean isWitchOn(String configItem) {
    return SWITCH_ON.equalsIgnoreCase(configItem);
  }


}
