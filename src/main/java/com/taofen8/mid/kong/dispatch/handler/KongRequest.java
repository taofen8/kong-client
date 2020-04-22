/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.dispatch.handler;

import com.taofen8.mid.kong.annotation.KongServiceMapping;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;

public class KongRequest<R> {

  final Map<String, String> requestHeaders;
  final Map<String, Cookie> requestCookies;
  final R requestBody;
  final KongServiceMapping serviceMapping;

  public Map<String, Cookie> requestCookies() {
    return this.requestCookies;
  }

  public Map<String, String> requestHeaders() {
    return this.requestHeaders;
  }

  public R requestBody() {
    return this.requestBody;
  }

  public KongServiceMapping serviceMapping() {
    return this.serviceMapping;
  }

  public KongRequest(Builder<R> builder) {
    this.requestHeaders = builder.requestHeaders;
    this.requestCookies = builder.requestCookies;
    this.requestBody = builder.requestBody;
    this.serviceMapping = builder.serviceMapping;
  }

  public static class Builder<R> {

    private Map<String, String> requestHeaders = new HashMap<>();
    private Map<String, Cookie> requestCookies = new HashMap<String, Cookie>();
    private R requestBody;
    private KongServiceMapping serviceMapping;

    public Builder<R> requestHeaders(Map<String, String> heads) {
      if (heads != null) {
        this.requestHeaders.putAll(heads);
      }
      return this;
    }

    public Builder<R> requestCookies(Map<String, Cookie> cookies) {
      this.requestCookies.putAll(cookies);
      return this;
    }

    public Builder<R> requestCookies(String key, Cookie cookie) {
      this.requestCookies.put(key, cookie);
      return this;
    }

    public Builder<R> requestBody(R requestBody) {
      this.requestBody = requestBody;
      return this;
    }

    public Builder<R> serviceMapping(KongServiceMapping serviceMapping) {
      this.serviceMapping = serviceMapping;
      return this;
    }

    public KongRequest<R> build() {
      return new KongRequest(this);
    }
  }
}
