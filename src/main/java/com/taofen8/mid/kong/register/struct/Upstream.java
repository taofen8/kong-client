/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.register.struct;

import com.taofen8.mid.kong.util.JsonUtil;
import java.util.List;
import java.util.Map;
import org.springframework.util.StringUtils;

public class Upstream {

  final String name;
  final String algorithm;
  final String hash_on;
  final String hash_fallback;
  final String hash_on_header;
  final String hash_fallback_header;
  final String hash_on_cookie;
  final String hash_on_cookie_path;
  final Integer slots;
  final List<String> tags;
  final String host_header;
  final Map healthcheckConfig;
  final Target target;

  public Upstream(Builder builder) {
    this.name = builder.name;
    this.algorithm = builder.algorithm;
    this.hash_on = builder.hash_on;
    this.hash_fallback = builder.hash_fallback;
    this.hash_on_header = builder.hash_on_header;
    this.hash_fallback_header = builder.hash_fallback_header;
    this.hash_on_cookie = builder.hash_on_cookie;
    this.hash_on_cookie_path = builder.hash_on_cookie_path;
    this.slots = builder.slots;
    this.tags = builder.tags;
    this.host_header = builder.host_header;
    this.healthcheckConfig = builder.healthcheckConfig;
    this.target = builder.target;
  }


  public String name() {
    return this.name;
  }

  public String algorithm() {
    return this.algorithm;
  }

  public String hashOn() {
    return this.hash_on;
  }

  public String hashFallback() {
    return this.hash_fallback;
  }

  public String hashOnHeader() {
    return this.hash_on_header;
  }

  public String hashFallbackHeader() {
    return this.hash_fallback_header;
  }

  public String hashOnCookie() {
    return this.hash_on_cookie;
  }

  public String hashOnCookiePath() {
    return this.hash_on_cookie_path;
  }

  public Integer slots() {
    return this.slots;
  }

  public List<String> tags() {
    return this.tags;
  }

  public String hostHeader() {
    return this.host_header;
  }

  public Target target() {
    return this.target;
  }

  public Map healthcheckConfig() {
    return this.healthcheckConfig;
  }


  public static class BalancerAlgorithm {
    private BalancerAlgorithm(){}
    public final static String CONSISTENT_HASHING = "consistent-hashing";
    public final static String LEAST_CONNECTIONS = "least-connections";
    public final static String ROUND_ROBIN = "round-robin";

  }

  public static class HashOn {
    private  HashOn(){}
    public final static String NONE = "none";
    public final static String CONSUMER = "consumer";
    public final static String IP = "ip";
    public final static String HEADER = "header";
    public final static String COOKIE = "cookie";
  }

  public static class Builder {

    private String name;
    private String algorithm = BalancerAlgorithm.ROUND_ROBIN;
    private String hash_on = HashOn.NONE;
    private String hash_fallback = HashOn.NONE;
    private String hash_on_header;
    private String hash_fallback_header;
    private String hash_on_cookie;
    private String hash_on_cookie_path = "/";
    private Integer slots = 10000;
    private List<String> tags;
    private String host_header;
    private Target target;
    private String healthcheckConfigString = "{\"active\":{\"unhealthy\":{\"http_statuses\":[429,404,500,501,502,503,504,505],\"tcp_failures\":3,\"timeouts\":3,\"http_failures\":3,\"interval\":3},\"type\":\"http\",\"http_path\":\"/__builtin/hi\",\"timeout\":3,\"healthy\":{\"successes\":1,\"interval\":1,\"http_statuses\":[200,302]},\"https_verify_certificate\":false,\"concurrency\":10},\"passive\":{\"unhealthy\":{\"http_failures\":0,\"http_statuses\":[429,500,503],\"tcp_failures\":0,\"timeouts\":0},\"healthy\":{\"http_statuses\":[200,201,202,203,204,205,206,207,208,226,300,301,302,303,304,305,306,307,308],\"successes\":0},\"type\":\"http\"}}";
    private Map healthcheckConfig;


    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder algorithm(String algorithm) {
      this.algorithm = algorithm;
      return this;
    }

    public Builder hashOn(String hash_on) {
      this.hash_on = hash_on;
      return this;
    }

    public Builder hashFallback(String hash_fallback) {
      this.hash_fallback = hash_fallback;
      return this;
    }

    public Builder hashOnHeader(String hash_on_header) {
      this.hash_on_header = hash_on_header;
      return this;
    }

    public Builder hashFallbackHeader(String hash_fallback_header) {
      this.hash_fallback_header = hash_fallback_header;
      return this;
    }

    public Builder hashOnCookie(String hash_on_cookie) {
      this.hash_on_cookie = hash_on_cookie;
      return this;
    }

    public Builder hashOnCookiePath(String hash_on_cookie_path) {
      this.hash_on_cookie_path = hash_on_cookie_path;
      return this;
    }

    public Builder slots(Integer slots) {
      this.slots = slots;
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

    public Builder target(Target target) {
      this.target = target;
      return this;
    }

    public Builder healthcheckConfig(String healthcheckConfig) {
      if (!StringUtils.isEmpty(healthcheckConfig)) {
        this.healthcheckConfigString = healthcheckConfig;
      }
      this.healthcheckConfig = JsonUtil.parseObject(this.healthcheckConfigString, Map.class);
      return this;
    }

    public Upstream build() {
      this.slots = (this.slots == 10000 ? 10000 : (Math.min(65536, Math.max(10, this.slots))));
      return new Upstream(this);
    }
  }
}
