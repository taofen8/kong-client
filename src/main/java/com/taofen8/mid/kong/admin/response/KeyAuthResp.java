/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin.response;

import com.taofen8.mid.kong.util.JsonUtil;
import java.util.Map;

public class KeyAuthResp extends BaseResp {

  private String id;
  private Map<String, String> consumer;
  private String key;
  private Long created_at;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Map<String, String> getConsumer() {
    return consumer;
  }

  public void setConsumer(Map<String, String> consumer) {
    this.consumer = consumer;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public Long getCreated_at() {
    return created_at;
  }

  public void setCreated_at(Long created_at) {
    this.created_at = created_at;
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
