/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin.response;


import com.taofen8.mid.kong.util.JsonUtil;
import java.util.Map;

public class TargetResp extends BaseResp {

  private String id;
  private String target;
  private Integer weight;
  private Map<String, Object> upstream;
  private Float created_at;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  public Integer getWeight() {
    return weight;
  }

  public void setWeight(Integer weight) {
    this.weight = weight;
  }

  public Map<String, Object> getUpstream() {
    return upstream;
  }

  public void setUpstream(Map<String, Object> upstream) {
    this.upstream = upstream;
  }

  public Float getCreated_at() {
    return created_at;
  }

  public void setCreated_at(Float created_at) {
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
