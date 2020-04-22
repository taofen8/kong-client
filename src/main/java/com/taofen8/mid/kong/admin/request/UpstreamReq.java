/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin.request;


import java.util.List;
import java.util.Map;

public class UpstreamReq extends BaseReq {

  final String name;
  final String algorithm;
  final Integer slots;
  final String hash_on;
  final String hash_fallback;
  final String hash_on_header;
  final String hash_fallback_header;
  final String hash_on_cookie;
  final String hash_on_cookie_path;
  final Map<String, Object> healthchecks;
  final List<String> tags;
  final String host_header;

  public String getName() {
    return name;
  }

  public String getAlgorithm() {
    return algorithm;
  }

  public Integer getSlots() {
    return slots;
  }

  public String getHash_on() {
    return hash_on;
  }

  public String getHash_fallback() {
    return hash_fallback;
  }

  public String getHash_on_header() {
    return hash_on_header;
  }

  public String getHash_fallback_header() {
    return hash_fallback_header;
  }

  public String getHash_on_cookie() {
    return hash_on_cookie;
  }

  public String getHash_on_cookie_path() {
    return hash_on_cookie_path;
  }

  public Map<String, Object> getHealthchecks() {
    return healthchecks;
  }

  public List<String> getTags() {
    return tags;
  }

  public String getHost_header() {
    return host_header;
  }

  public UpstreamReq(Builder builder) {
    this.name = builder.name;
    this.algorithm = builder.algorithm;
    this.slots = builder.slots;
    this.hash_on = builder.hash_on;
    this.hash_fallback = builder.hash_fallback;
    this.hash_on_header = builder.hash_on_header;
    this.hash_fallback_header = builder.hash_fallback_header;
    this.hash_on_cookie = builder.hash_on_cookie;
    this.hash_on_cookie_path = builder.hash_on_cookie_path;
    this.healthchecks = builder.healthchecks;
    this.tags = builder.tags;
    this.host_header = builder.host_header;
  }

  public static class Builder {

    private String name;
    private String algorithm;
    private Integer slots = 1000;
    private String hash_on;
    private String hash_fallback;
    private String hash_on_header;
    private String hash_fallback_header;
    private String hash_on_cookie;
    private String hash_on_cookie_path;
    private Map<String, Object> healthchecks;
    private List<String> tags;
    private String host_header;

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder algorithm(String algorithm) {
      this.algorithm = algorithm;
      return this;
    }

    public Builder slots(Integer slots) {
      this.slots = slots;
      return this;
    }

    public Builder hashOn(String hashOn) {
      this.hash_on = hashOn;
      return this;
    }

    public Builder hashFallback(String hashFallback) {
      this.hash_fallback = hashFallback;
      return this;
    }

    public Builder hashOnHeader(String hashOnHeader) {
      this.hash_on_header = hashOnHeader;
      return this;
    }

    public Builder hashFallbackHeader(String hashFallbackHeader) {
      this.hash_fallback_header = hashFallbackHeader;
      return this;
    }

    public Builder hashOnCookie(String hashOnCookie) {
      this.hash_on_cookie = hashOnCookie;
      return this;
    }

    public Builder hashOnCookiePath(String hashOnCookiePath) {
      this.hash_on_cookie_path = hashOnCookiePath;
      return this;
    }

    public Builder healthchecks(Map<String, Object> healthchecks) {
      this.healthchecks = healthchecks;
      return this;
    }

    public Builder tags(List<String> tags) {
      this.tags = tags;
      return this;
    }

    public Builder hostHeader(String host_header) {
      this.host_header = host_header;
      return this;
    }

    public UpstreamReq build() {
      return new UpstreamReq(this);
    }
  }
}
