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

public class KongResponse<S> {

  final Map<String, String> responseHeaders;
  final Map<String, Cookie> responseCookies;
  final S responseBody;
  final int status;
  final String errorMessage;
  final KongServiceMapping serviceMapping;

  public Map<String, String> responseHeaders() {
    return responseHeaders;
  }

  public Map<String, Cookie> responseCookies() {
    return responseCookies;
  }

  public S responseBody() {
    return responseBody;
  }

  public int status() {
    return status;
  }

  public String errorMessage() {
    return errorMessage;
  }

  public KongServiceMapping serviceMapping() {
    return this.serviceMapping;
  }


  public KongResponse(Builder<S> builder) {
    this.responseHeaders = builder.responseHeaders;
    this.responseCookies = builder.responseCookies;
    this.responseBody = builder.responseBody;
    this.serviceMapping = builder.serviceMapping;
    this.errorMessage = builder.errorMessage;
    this.status = builder.status;
  }

  public static class Builder<S> {

    private Map<String, String> responseHeaders = new HashMap<String, String>();
    private Map<String, Cookie> responseCookies = new HashMap<String, Cookie>();
    private S responseBody;
    private int status = 200;
    private String errorMessage;
    private KongServiceMapping serviceMapping;

    public Builder<S> responseHeaders(Map<String, String> responseHeaders) {
      this.responseHeaders.putAll(responseHeaders);
      return this;
    }

    public Builder<S> responseHeaders(String key, String value) {
      this.responseHeaders.put(key, value);
      return this;
    }

    public Builder<S> responseCookies(Map<String, Cookie> cookies) {
      this.responseCookies.putAll(cookies);
      return this;
    }

    public Builder<S> responseCookies(String key, Cookie cookie) {
      this.responseCookies.put(key, cookie);
      return this;
    }

    public Builder<S> responseBody(S responseBody) {
      this.responseBody = responseBody;
      return this;
    }

    public Builder<S> status(int status) {
      this.status = status;
      return this;
    }

    public Builder<S> errorMessage(String errorMessage) {
      this.errorMessage = errorMessage;
      return this;
    }

    public Builder<S> serviceMapping(KongServiceMapping serviceMapping) {
      this.serviceMapping = serviceMapping;
      return this;
    }

    public KongResponse<S> build() {
      return new KongResponse(this);
    }
  }
}
