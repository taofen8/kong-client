/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.dispatch.handler.impl;

import com.taofen8.mid.kong.dispatch.handler.ResponseBodyIntercepter;
import com.taofen8.mid.kong.dispatch.handler.KongRequest;
import com.taofen8.mid.kong.dispatch.handler.KongResponse;
import com.taofen8.mid.kong.util.GzipUtil;

public class ResponseGzipIntercepter implements ResponseBodyIntercepter {

  @Override
  public byte[] intercept(byte[] bytesOfBody, KongRequest request, KongResponse response) {
    if (bytesOfBody != null) {
      byte[] bytes = GzipUtil.zip(bytesOfBody);
      response.responseHeaders().put("Content-Encoding",
          "gzip");
      return bytes;
    }
    return bytesOfBody;
  }
}