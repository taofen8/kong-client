/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.caller;

import com.taofen8.mid.kong.caller.exception.LoadBalancerException;
import com.taofen8.mid.kong.util.GzipUtil;
import com.taofen8.mid.kong.util.JsonUtil;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.util.StringUtils;

public final class CocoRequest {

  final String host;
  final String path;
  final Map<String, Object> biz;
  final Map<String, String> headers;
  static final MediaType MEDIA_TYPE
      = MediaType.parse("application/json; charset=utf-8");

  public CocoRequest(CocoRequest.Builder builder) {
    this.host = builder.host;
    this.biz = builder.biz;
    this.headers = builder.headers;
    this.path = builder.path;
  }

  public Request buildOkHttpRequest(CocoClient client) throws LoadBalancerException {
    this.headers.putAll(client.globalHeaders());
    this.headers.put("Host", host);

    if (client.enableRequestGzip) {
      this.headers.put("Content-Encoding", "gzip");
    }

    if (!StringUtils.isEmpty(client.getConsumerKey()) && !StringUtils
        .isEmpty(client.getConsumerKeyName())) {
      this.headers.put(client.getConsumerKeyName(), client.getConsumerKey());
    }

    Request.Builder builder = new Request.Builder()
        .headers(Headers.of(this.headers))
        .url(client.getNextNode() + path);

    final byte[] content = client.codec() != null ? client.codec()
        .encode(JsonUtil.toJSONString(biz).getBytes())
        : JsonUtil.toJSONString(biz).getBytes();

    builder.post(RequestBody
        .create(MEDIA_TYPE, client.enableRequestGzip ? GzipUtil.zip(content) : content));
    return builder.build();
  }

  public static class Builder {

    private Map<String, Object> biz = new HashMap<String, Object>();
    private Map<String, String> headers = new HashMap<String, String>();
    private String path;
    private String host;

    public Builder host(String host) {
      this.host = host;
      return this;
    }

    public Builder path(String path) {
      if (!path.startsWith("/")) {
        this.path = "/" + path;
      } else {
        this.path = path;
      }
      return this;
    }

    public Builder headers(String key, String value) {
      headers.put(key, value);
      return this;
    }

    public Builder headers(Map<String, String> headers) {
      headers.putAll(headers);
      return this;
    }

    public Builder biz(String key, String Object) {
      this.biz.put(key, Object);
      return this;
    }

    public Builder biz(String key, Object Object) {
      this.biz.put(key, Object);
      return this;
    }

    public Builder biz(Map<String, Object> biz) {
      this.biz.putAll(biz);
      return this;
    }


    public CocoRequest build() {
      return new CocoRequest(this);
    }
  }
}
