/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.config.internal;

import java.util.Objects;
import java.util.Properties;

public class InternalConfig {

  private Properties _property = new Properties();

  public InternalConfig configure(InternalConfigEntry configFeature, String value) {
    Objects.requireNonNull(configFeature);
    _property.put(configFeature.getName(), value);
    return this;
  }

  public String getConfig(InternalConfigEntry configFeature) {
    Objects.requireNonNull(configFeature);
    return _property.getProperty(configFeature.getName());
  }

  public enum InternalConfigEntry {
    COMSUMER_KEY("consumer_key"),
    IP("ip"),
    PORT("port");
    private String name;

    public String getName() {
      return name;
    }

    InternalConfigEntry(String name) {
      this.name = name;
    }

  }

}

