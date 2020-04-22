/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.register.struct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.util.CollectionUtils;

public class Route {

  final String name;
  final List<String> protocols;
  final List<RequestMethod> methods;
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


  public Route(Route.Builder builder) {
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
  }

  public String name() {
    return this.name;
  }

  public List<String> protocols() {
    return protocols;
  }

  public List<RequestMethod> methods() {
    return methods;
  }

  public List<String> hosts() {
    return this.hosts;
  }

  public List<String> paths() {
    return this.paths;
  }

  public Map<String, List<String>> headers() {
    return headers;
  }

  public Integer httpsRedirectStatusCode() {
    return https_redirect_status_code;
  }


  public Integer regexPriority() {
    return regex_priority;
  }

  public Boolean stripPath() {
    return strip_path;
  }

  public String pathHandling() {
    return path_handling;
  }


  public Boolean preserveHost() {
    return preserve_host;
  }

  public List<String> snis() {
    return snis;
  }

  public List<Map<String, Object>> sources() {
    return sources;
  }

  public List<Map<String, Object>> destinations() {
    return destinations;
  }

  public List<String> tags() {
    return tags;
  }


  public static class Builder {

    private String name;
    private List<String> protocols;
    private List<RequestMethod> methods;
    private List<String> hosts;
    private List<String> paths;
    private Map<String, List<String>> headers;
    private Integer https_redirect_status_code;
    private Integer regex_priority = 0;
    private Boolean strip_path = false;
    private String path_handling;
    private Boolean preserve_host = false;
    private List<String> snis;
    private List<Map<String, Object>> sources;
    private List<Map<String, Object>> destinations;
    private List<String> tags;


    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder protocols(List<String> protocols) {
      if (!CollectionUtils.isEmpty(protocols)) {
        this.protocols = protocols;
      }
      return this;
    }

    public Builder methods(List<RequestMethod> methods) {
      if (!CollectionUtils.isEmpty(methods)) {
        this.methods = methods;
      }
      return this;
    }

    public Builder hosts(List<String> hosts) {
      if (!CollectionUtils.isEmpty(hosts)) {
        this.hosts = hosts;
      }
      return this;
    }

    public Builder paths(List<String> paths) {
      if (!CollectionUtils.isEmpty(paths)) {
        this.paths = paths;
      }
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
      if (!CollectionUtils.isEmpty(headers)) {
        this.headers = headers;
      }
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
      if (!CollectionUtils.isEmpty(snis)) {
        this.snis = snis;
      }
      return this;
    }

    public Builder sources(List<Map<String, Object>> sources) {
      if (!CollectionUtils.isEmpty(sources)) {
        this.sources = sources;
      }
      return this;
    }

    public Builder destinations(List<Map<String, Object>> destinations) {
      if (!CollectionUtils.isEmpty(destinations)) {
        this.destinations = destinations;
      }
      return this;
    }

    public Builder tags(List<String> tags) {
      if (!CollectionUtils.isEmpty(tags)) {
        this.tags = tags;
      }
      return this;
    }

    public Route build() {
      if (this.protocols == null) {
        this.protocols = new ArrayList<String>(4);
        this.protocols.add("http");
        this.protocols.add("https");
      }
      return new Route(this);
    }

  }

}
