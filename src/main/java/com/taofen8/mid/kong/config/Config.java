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
    if (_property.containsKey(configFeature.getName())) {
      return _property.getProperty(configFeature.getName());
    }
    return configFeature.getDefaultValue();
  }

  public enum ConfigEntry {
    /******* server  config ********/

    KONG_SERVER_ADMIN_URL("kong.config.server.admin.url", null),
    KONG_SERVER_APP_IDENTIFIER("kong.config.server.app.identifier", null),
    KONG_SERVER_ROUTE_HOSTS("kong.config.server.route.hosts", null),
    KONG_SERVER_PROXY_STRATEGY("kong.config.server.proxy.strategy", null),
    KONG_SERVER_HEALTHCHECKS("kong.config.server.healthcheck.config",
        "{\"active\":{\"unhealthy\":{\"http_statuses\":[429,404,500,501,502,503,504,505],\"tcp_failures\":3,\"timeouts\":3,\"http_failures\":3,\"interval\":3},\"type\":\"http\",\"http_path\":\"/__builtin/hi\",\"timeout\":3,\"healthy\":{\"successes\":1,\"interval\":1,\"http_statuses\":[200,302]},\"https_verify_certificate\":false,\"concurrency\":10},\"passive\":{\"unhealthy\":{\"http_failures\":0,\"http_statuses\":[429,500,503],\"tcp_failures\":0,\"timeouts\":0},\"healthy\":{\"http_statuses\":[200,201,202,203,204,205,206,207,208,226,300,301,302,303,304,305,306,307,308],\"successes\":0},\"type\":\"http\"}}"),
    KONG_SERVER_ADDRESS_AUTO_RESOLVE("kong.config.server.address.resolve", "on"),

    /******* caller  config ********/
    KONG_CALLER_BALANCER_NODES("kong.config.caller.balancer.nodes", null),
    KONG_CALLER_KEYAUTH_ENABLE("kong.config.caller.keyauth.enable", "off"),
    KONG_CALLER_KEYAUTH_KEYNAME("kong.config.caller.keyauth.keyname", "apikey");

    private String name;
    private String defaultValue;

    public String getName() {
      return name;
    }

    public String getDefaultValue() {
      return defaultValue;
    }

    ConfigEntry(String name, String defaultValue) {
      this.name = name;
      this.defaultValue = defaultValue;
    }

  }

}
