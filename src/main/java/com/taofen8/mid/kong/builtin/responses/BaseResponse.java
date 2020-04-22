/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.builtin.responses;

public class BaseResponse {

  String provider;

  public String getProvider() {
    return provider;
  }

  public void setProvider(String provider) {
    this.provider = provider;
  }

  BaseResponse() {
    this.provider = "built-in api @taofen8 kong";
  }
}
