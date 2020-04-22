/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin.response;


import com.taofen8.mid.kong.util.JsonUtil;

public class EndpointResp extends BaseResp {

  private Long created_at;
  private String id;
  private String health;
  private String target;
  private String upstream_id;
  private Integer weight;

  public Long getCreated_at() {
    return created_at;
  }

  public void setCreated_at(Long created_at) {
    this.created_at = created_at;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getHealth() {
    return health;
  }

  public void setHealth(String health) {
    this.health = health;
  }

  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  public String getUpstream_id() {
    return upstream_id;
  }

  public void setUpstream_id(String upstream_id) {
    this.upstream_id = upstream_id;
  }

  public Integer getWeight() {
    return weight;
  }

  public void setWeight(Integer weight) {
    this.weight = weight;
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
