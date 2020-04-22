/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin.request;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteReq extends BaseReq {

  final String name;
  final List<String> protocols;
  final List<String> methods;
  final List<String> hosts;
  final List<String> paths;
  final Map<String, List<String>> headers;
  final Integer https_redirect_status_code;
  final Integer regex_priority;
  final Boolean strip_path;
  final String path_handling;
  final Boolean preserve_host;
  final List<String> snis;
  final List<Map<String, Object>> sources;
  final List<Map<String, Object>> destinations;
  final List<String> tags;
  final Map<String, Object> service;

  public RouteReq(Builder builder) {
    this.name = builder.name;
    this.protocols = builder.protocols;
    this.methods = builder.methods;
    this.hosts = builder.hosts;
    this.paths = builder.paths;
    this.headers = builder.headers;
    this.https_redirect_status_code = builder.https_redirect_status_code;
    this.regex_priority = builder.regex_priority;
    this.strip_path = builder.strip_path;
    this.path_handling = builder.path_handling;
    this.preserve_host = builder.preserve_host;
    this.snis = builder.snis;
    this.sources = builder.sources;
    this.destinations = builder.destinations;
    this.tags = builder.tags;
    this.service = builder.service;
  }


  public String getName() {
    return name;
  }

  public List<String> getProtocols() {
    return protocols;
  }

  public List<String> getMethods() {
    return methods;
  }

  public List<String> getHosts() {
    return hosts;
  }

  public List<String> getPaths() {
    return paths;
  }

  public Map<String, List<String>> getHeaders() {
    return headers;
  }

  public Integer getHttps_redirect_status_code() {
    return https_redirect_status_code;
  }

  public Integer getRegex_priority() {
    return regex_priority;
  }

  public Boolean getStrip_path() {
    return strip_path;
  }

  public String getPath_handling() {
    return path_handling;
  }

  public Boolean getPreserve_host() {
    return preserve_host;
  }

  public List<String> getSnis() {
    return snis;
  }

  public List<Map<String, Object>> getSources() {
    return sources;
  }

  public List<Map<String, Object>> getDestinations() {
    return destinations;
  }

  public List<String> getTags() {
    return tags;
  }

  public Map<String, Object> getService() {
    return service;
  }

  public static class Builder {

    private String name;
    private List<String> protocols;
    private List<String> methods;
    private List<String> hosts;
    private List<String> paths;
    private Map<String, List<String>> headers;
    private Integer https_redirect_status_code;
    private Integer regex_priority;
    private Boolean strip_path;
    private String path_handling;
    private Boolean preserve_host;
    private List<String> snis;
    private List<Map<String, Object>> sources;
    private List<Map<String, Object>> destinations;
    private List<String> tags;
    private Map<String, Object> service;

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder protocols(List<String> protocols) {
      this.protocols = protocols;
      return this;
    }

    public Builder protocol(String protocol) {
      if (this.protocols == null) {
        this.protocols = new ArrayList<>(4);
      }
      this.protocols.add(protocol);
      return this;
    }

    public Builder methods(List<String> methods) {
      this.methods = methods;
      return this;
    }

    public Builder method(String method) {
      if (this.methods == null) {
        this.methods = new ArrayList<>(4);
      }
      this.methods.add(method);
      return this;
    }

    public Builder hosts(List<String> hosts) {
      this.hosts = hosts;
      return this;
    }

    public Builder host(String host) {
      if (this.hosts == null) {
        this.hosts = new ArrayList<>(4);
      }
      this.hosts.add(host);
      return this;
    }

    public Builder paths(List<String> paths) {
      this.paths = paths;
      return this;
    }

    public Builder path(String path) {
      if (this.paths == null) {
        this.paths = new ArrayList<>(4);
      }
      this.paths.add(path);
      return this;
    }

    public Builder headers(Map<String, List<String>> headers) {
      this.headers = headers;
      return this;
    }

    public Builder httpsRedirectStatusCode(Integer https_redirect_status_code) {
      this.https_redirect_status_code = https_redirect_status_code;
      return this;
    }

    public Builder regexPriority(Integer regex_priority) {
      this.regex_priority = regex_priority;
      return this;
    }

    public Builder stripPath(Boolean strip_path) {
      this.strip_path = strip_path;
      return this;
    }

    public Builder pathHandling(String path_handling) {
      this.path_handling = path_handling;
      return this;
    }

    public Builder preserveHost(Boolean preserve_host) {
      this.preserve_host = preserve_host;
      return this;
    }

    public Builder snis(List<String> snis) {
      this.snis = snis;
      return this;
    }

    public Builder sources(List<Map<String, Object>> sources) {
      this.sources = sources;
      return this;
    }

    public Builder destinations(List<Map<String, Object>> destinations) {
      this.destinations = destinations;
      return this;
    }

    public Builder tags(List<String> tags) {
      this.tags = tags;
      return this;
    }

    public Builder serviceId(String serviceId) {
      if (this.service == null) {
        this.service = new HashMap<>(2);
      }
      this.service.put("id", serviceId);
      return this;
    }

    public RouteReq build() {
      return new RouteReq(this);
    }
  }
}
