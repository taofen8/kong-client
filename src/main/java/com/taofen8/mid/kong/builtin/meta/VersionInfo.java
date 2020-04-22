/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.builtin.meta;

import java.io.InputStream;
import java.util.Properties;

public class VersionInfo {

  private static String version;
  final static String PATH = "/META-INF/maven/com.tfg.mid/kong-client/pom.properties";

  private VersionInfo() {
  }

  public static String getVersion() {
    return version;
  }

  public static void loadVersion() {
    try {
      InputStream inStream = VersionInfo.class.getClassLoader().getResourceAsStream(PATH);
      Properties properties = new Properties();
      properties.load(inStream);
      version = properties.getProperty("version");
    } catch (Exception e) {
      //nothing
    }
  }
}
