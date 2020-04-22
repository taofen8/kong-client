/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin.query;

public class PluginQuery {

  final String id;
  final String name;
  final String service_id;
  final String route_id;
  final String consumer_id;
  final Integer size;
  final String offset;

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getService_id() {
    return service_id;
  }

  public String getRoute_id() {
    return route_id;
  }

  public String getConsumer_id() {
    return consumer_id;
  }

  public Integer getSize() {
    return size;
  }

  public String getOffset() {
    return offset;
  }

  public PluginQuery(Builder builder) {
    this.id = builder.id;
    this.name = builder.name;
    this.service_id = builder.service_id;
    this.route_id = builder.route_id;
    this.consumer_id = builder.consumer_id;
    this.size = builder.size;
    this.offset = builder.offset;
  }


  public static class Builder {

    private String id;
    private String name;
    private String service_id;
    private String route_id;
    private String consumer_id;
    private Integer size = 100;
    private String offset;

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder serviceId(String serviceId) {
      this.service_id = serviceId;
      return this;
    }

    public Builder routeId(String routeId) {
      this.route_id = routeId;
      return this;
    }

    public Builder consumerId(String consumeId) {
      this.consumer_id = consumeId;
      return this;
    }

    public Builder size(Integer size) {
      this.size = size;
      return this;
    }

    public Builder offset(String offset) {
      this.offset = offset;
      return this;
    }

    public PluginQuery build() {
      return new PluginQuery(this);
    }
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder("?size=" + this.size);
    if (this.id != null) {
      builder.append("&id=" + this.id);
    }
    if (this.name != null) {
      builder.append("&name=" + this.name);
    }
    if (this.service_id != null) {
      builder.append("&service_id=" + this.service_id);
    }
    if (this.route_id != null) {
      builder.append("&route_id=" + this.route_id);
    }
    if (this.consumer_id != null) {
      builder.append("&consumer_id=" + this.consumer_id);
    }
    if (this.offset != null) {
      builder.append("&offset=" + this.offset);
    }

    return builder.toString();
  }
}
