/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin.request;


import java.util.List;

public class ServiceReq extends BaseReq {

  final String name;
  final String protocol;
  final String host;
  final Integer port;
  final String path;
  final Integer retries;
  final Integer connect_timeout;
  final Integer write_timeout;
  final Integer read_timeout;
  final String url;
  final List<String> tags;

  public String getName() {
    return name;
  }

  public String getProtocol() {
    return protocol;
  }

  public String getHost() {
    return host;
  }

  public Integer getPort() {
    return port;
  }

  public String getPath() {
    return path;
  }

  public Integer getRetries() {
    return retries;
  }

  public Integer getConnect_timeout() {
    return connect_timeout;
  }

  public Integer getWrite_timeout() {
    return write_timeout;
  }

  public Integer getRead_timeout() {
    return read_timeout;
  }

  public String getUrl() {
    return url;
  }

  public List<String> getTags() {
    return tags;
  }

  public ServiceReq(Builder builder) {
    this.name = builder.name;
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
  }

  public static class Builder {

    private String name;
    private String protocol;
    private String host;
    private Integer port;
    private String path;
    private Integer retries;
    private Integer connect_timeout;
    private Integer write_timeout;
    private Integer read_timeout;
    private String url;
    private List<String> tags;


    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder protocol(String protocol) {
      this.protocol = protocol;
      return this;
    }

    public Builder host(String host) {
      this.host = host;
      return this;
    }

    public Builder port(Integer port) {
      this.port = port;
      return this;
    }

    public Builder path(String path) {
      this.path = path;
      return this;
    }

    public Builder retries(Integer retries) {
      this.retries = retries;
      return this;
    }

    public Builder tags(List<String> tags) {
      this.tags = tags;
      return this;
    }

    public Builder connectTimeout(Integer connectTimeout) {
      this.connect_timeout = connectTimeout;
      return this;
    }

    public Builder writeTimeout(Integer writeTimeout) {
      this.write_timeout = writeTimeout;
      return this;
    }

    public Builder readTimeout(Integer readTimeout) {
      this.read_timeout = readTimeout;
      return this;
    }

    public Builder url(String url) {
      this.url = url;
      return this;
    }

    public ServiceReq build() {
      return new ServiceReq(this);
    }

  }
}
