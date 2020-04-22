/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.caller;

import com.taofen8.mid.kong.caller.exception.CallerDecodeException;
import com.taofen8.mid.kong.util.GzipUtil;
import com.taofen8.mid.kong.util.JsonUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CocoResponse {

  private Logger logger = LoggerFactory.getLogger(CocoResponse.class);

  final Map<String, String> headers;
  final String body;
  final String errMsg;
  final int status;
  private Map<String, Object> mapBody;

  public Map<String, Object> mapBody() {
    try {
      if (mapBody == null && body != null) {
        mapBody = JsonUtil.parseObject(body, Map.class);
      }
    } catch (Exception e) {
      logger.error("CocoResponse get mapBody error ,", e);
    }
    if (mapBody == null) {
      mapBody = new HashMap<>(2);
    }
    return mapBody;
  }

  public Map<String, String> headers() {
    return headers;
  }

  public String errMsg() {
    return errMsg;
  }

  public String body() {
    return body;
  }

  public int status() {
    return status;
  }

  public boolean isSuccessful() {
    return status >= 200 && status < 300;
  }

  public CocoResponse(int status, String errMsg) {
    this.status = status;
    this.errMsg = errMsg;
    this.body = null;
    this.headers = null;
  }

  public CocoResponse(Response response, CocoClient client) throws IOException {
    int code = response.code();
    String error = null;
    String responseBody = null;

    if (response.isSuccessful()) {
      try {
        byte[] theBody = response.body().bytes();
        if ("gzip".equalsIgnoreCase(response.header("Content-Encoding")) || "gzip".equalsIgnoreCase(response.header("content-encoding"))) {
          theBody = GzipUtil.unzip(theBody);
        }
        theBody = client.codec() != null ? client.codec().decode(theBody)
            : theBody;
        responseBody = new String(theBody);
      } catch (CallerDecodeException e) {
        code = 500;
        error = "Decode error," + e.getMessage();
      }
    } else {
      error = new String(response.body().bytes());
    }

    Map<String, String> headersMap = new HashMap<>(4);
    if (response.headers() != null) {
      Iterator<String> iterator = response.headers().names().iterator();
      while (iterator.hasNext()) {
        String key = iterator.next();
        headersMap.put(key, response.header(key));
      }
    }

    this.status = code;
    this.errMsg = error;
    this.headers = headersMap;
    this.body = responseBody;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("CocoResponse >\n");
    builder.append("status: ");
    builder.append(this.status);
    builder.append("\n");
    builder.append("errMsg: ");
    builder.append(this.errMsg);
    builder.append("\n");
    builder.append("body: ");
    builder.append(this.body);
    builder.append("\n");
    builder.append("headers: ");
    builder.append(headers == null ? "" : JsonUtil.toJSONString(this.headers));

    return builder.toString();
  }


}
