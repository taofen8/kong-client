/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin.response;


import com.taofen8.mid.kong.util.JsonUtil;
import java.util.List;

public class UpstreamPageResp extends BaseResp {

  private List<UpstreamResp> data;
  private String next;
  private String offset;
  private Integer total;

  public List<UpstreamResp> getData() {
    return data;
  }

  public void setData(List<UpstreamResp> data) {
    this.data = data;
  }

  public String getNext() {
    return next;
  }

  public void setNext(String next) {
    this.next = next;
  }

  public String getOffset() {
    return offset;
  }

  public void setOffset(String offset) {
    this.offset = offset;
  }

  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
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
