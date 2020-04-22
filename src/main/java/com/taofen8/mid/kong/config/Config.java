/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.config;

import java.util.Objects;
import java.util.Properties;

public final class Config {

  private Properties _property = new Properties();

  public Config configure(ConfigEntry configFeature, String value) {
    Objects.requireNonNull(configFeature);
    _property.put(configFeature.getName(), value);
    return this;
  }

  public String getConfig(ConfigEntry configFeature) {
    Objects.requireNonNull(configFeature);
    return _property.getProperty(configFeature.getName());
  }

  public enum ConfigEntry {
    /******* server  config ********/
    KONG_SERVER_ADMIN_URL("kong.config.server.admin.url"),
    KONG_SERVER_APP_IDENTIFIER("kong.config.server.app.identifier"),
    KONG_SERVER_ROUTE_HOSTS("kong.config.server.route.hosts"),
    KONG_SERVER_PROXY_STRATEGY("kong.config.server.proxy.strategy"),
    KONG_SERVER_HEALTHCHECKS("kong.config.server.healthcheck.config"),

    /******* caller  config ********/
    KONG_CALLER_BALANCER_NODES("kong.config.caller.balancer.nodes"),
    KONG_CALLER_KEYAUTH_ENABLE("kong.config.caller.keyauth.enable"),
    KONG_CALLER_KEYAUTH_KEYNAME("kong.config.caller.keyauth.keyname");

    private String name;

    public String getName() {
      return name;
    }

    ConfigEntry(String name) {
      this.name = name;
    }

  }

}
