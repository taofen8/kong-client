/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.builtin.responses;

public class HiResponse extends BaseResponse {

  private String say;

  public String getSay() {
    return say;
  }

  public void setSay(String say) {
    this.say = say;
  }
}
