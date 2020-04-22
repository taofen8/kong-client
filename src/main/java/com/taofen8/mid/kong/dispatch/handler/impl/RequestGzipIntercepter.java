/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.dispatch.handler.impl;

import com.taofen8.mid.kong.dispatch.handler.KongRequest;
import com.taofen8.mid.kong.dispatch.handler.RequestBodyIntercepter;
import com.taofen8.mid.kong.util.GzipUtil;

public class RequestGzipIntercepter implements RequestBodyIntercepter {

  @Override
  public byte[] intercept(byte[] bytesOfBody, KongRequest request) {
    if (bytesOfBody != null) {
      if ("gzip"
          .equalsIgnoreCase(
              (String) request.requestHeaders().get("content-encoding")) || "gzip"
          .equalsIgnoreCase(
              (String) request.requestHeaders().get("Content-Encoding"))) {
        bytesOfBody = GzipUtil.unzip(bytesOfBody);
      }
      return bytesOfBody;
    }
    return bytesOfBody;
  }
}