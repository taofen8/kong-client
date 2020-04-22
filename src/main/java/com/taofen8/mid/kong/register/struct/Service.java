/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.register.struct;

import java.util.List;


public class Service {

  final String name;
  final Integer retries;
  final String protocol;
  final String host;
  final Integer port;
  final String path;
  final Integer connect_timeout;
  final Integer write_timeout;
  final Integer read_timeout;
  final List<String> tags;
  final String url;
  final Upstream upstream;
  final Route route;

  public Service(Builder builder) {
    this.name = builder.host + "--" + builder.name;
    this.retries = builder.retries;
    this.protocol = builder.protocol;
    this.host = builder.host;
    this.port = builder.port;
    this.path = builder.path;
    this.connect_timeout = builder.connect_timeout;
    this.write_timeout = builder.write_timeout;
    this.read_timeout = builder.read_timeout;
    this.tags = builder.tags;
    this.url = builder.url;
    this.upstream = builder.upstream;
    this.route = builder.route;
  }

  public String name() {
    return name;
  }

  public Integer retries() {
    return retries;
  }

  public String protocol() {
    return protocol;
  }

  public String host() {
    return host;
  }

  public String path() {
    return path;
  }

  public Integer port() {
    return port;
  }

  public Integer connectTimeout() {
    return connect_timeout;
  }

  public Integer writeTimeout() {
    return write_timeout;
  }

  public Integer readTimeout() {
    return read_timeout;
  }

  public List<String> tags() {
    return tags;
  }

  public String url() {
    return url;
  }

  public Upstream upstream() {
    return upstream;
  }

  public Route route() {
    return route;
  }

  public static class Builder<T extends Builder<T>> {

    private String name;
    private Integer retries = 5;
    private String protocol = "http";
    private String host;
    private Integer port = 80;
    private String path;
    private Integer connect_timeout = 60000;
    private Integer write_timeout = 60000;
    private Integer read_timeout = 60000;
    private List<String> tags;
    private String url;
    private Upstream upstream;
    private Route route;

    public T name(String name) {
      this.name = name;
      return self();
    }

    public T name(Integer retries) {
      this.retries = retries;
      return self();
    }

    public T protocol(String protocol) {
      this.protocol = protocol;
      return self();
    }

    public T host(String host) {
      this.host = host;
      return self();
    }

    public T port(Integer port) {
      this.port = port;
      return self();
    }

    public T connectTimeout(Integer connect_timeout) {
      this.connect_timeout = connect_timeout;
      return self();
    }

    public T writeTimeout(Integer write_timeout) {
      this.write_timeout = write_timeout;
      return self();
    }

    public T readTimeout(Integer read_timeout) {
      this.read_timeout = read_timeout;
      return self();
    }

    public T tags(List<String> tags) {
      this.tags = tags;
      return self();
    }

    public T url(String url) {
      this.url = url;
      return self();
    }

    public T upstream(Upstream upstream) {
      this.upstream = upstream;
      return self();
    }

    public T route(Route route) {
      this.route = route;
      return self();
    }

    public Service build() {
      return new Service(this);
    }

    private T self() {
      return (T) this;
    }
  }

}
