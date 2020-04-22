/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin.request;


import java.util.HashMap;
import java.util.Map;

public class PluginReq extends BaseReq {

  final String name;
  final String consumer_id;
  final Map<String, Object> service;
  final String route_id;
  final Map<String, Object> config;
  final Boolean enabled;

  public String getName() {
    return name;
  }

  public String getConsumer_id() {
    return consumer_id;
  }

  public Map<String, Object> getService() {
    return service;
  }

  public String getRoute_id() {
    return route_id;
  }

  public Map<String, Object> getConfig() {
    return config;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public PluginReq(Builder builder) {
    this.name = builder.name;
    this.consumer_id = builder.consumer_id;
    this.service = builder.service;
    this.route_id = builder.route_id;
    this.config = builder.config;
    this.enabled = builder.enabled;
  }


  public static class Builder {

    private String name;
    private String consumer_id;
    private Map<String, Object> service;
    private String route_id;
    private Map<String, Object> config;
    private Boolean enabled;

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder consumerId(String consumerId) {
      this.consumer_id = consumerId;
      return this;
    }

    public Builder serviceId(String serviceId) {
      if (this.service == null) {
        this.service = new HashMap<String, Object>();
      }
      this.service.put("id", serviceId);
      return this;
    }

    public Builder routeId(String routeId) {
      this.route_id = routeId;
      return this;
    }

    public Builder config(Map<String, Object> config) {
      this.config = config;
      return this;
    }

    public Builder config(String key, Object value) {
      if (this.config == null) {
        this.config = new HashMap<String, Object>();
      }
      this.config.put(key, value);
      return this;
    }

    public Builder enabled(Boolean enabled) {
      this.enabled = enabled;
      return this;
    }

    public PluginReq build() {
      return new PluginReq(this);
    }
  }
}
