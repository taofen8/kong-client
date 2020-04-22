/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin.response;

import com.taofen8.mid.kong.util.JsonUtil;
import java.util.List;

public class KeyAuthPageResp extends  BaseResp {
  private List<KeyAuthResp> data;
  private String next;

  public String getNext() {
    return next;
  }

  public void setNext(String next) {
    this.next = next;
  }

  public List<KeyAuthResp> getData() {
    return data;
  }

  public void setData(List<KeyAuthResp> data) {
    this.data = data;
  }

  @Override
  public String toString() {
    String header = super.toString();

    StringBuilder builder = new StringBuilder();
    builder.append(header);
    builder.append("\n");
    builder.append(JsonUtil.toJSONString(this));
    return builder.toString();
  }
}
