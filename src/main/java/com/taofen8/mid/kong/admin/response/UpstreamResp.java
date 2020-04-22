/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin.response;


import com.taofen8.mid.kong.util.JsonUtil;
import java.util.Map;

public class UpstreamResp extends BaseResp {

  private String id;
  private String name;
  private String hash_on;
  private String hash_fallback;
  private Map<String, Object> healthchecks;
  private Integer slots;
  private Float created_at;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getHash_on() {
    return hash_on;
  }

  public void setHash_on(String hash_on) {
    this.hash_on = hash_on;
  }

  public String getHash_fallback() {
    return hash_fallback;
  }

  public void setHash_fallback(String hash_fallback) {
    this.hash_fallback = hash_fallback;
  }

  public Map<String, Object> getHealthchecks() {
    return healthchecks;
  }

  public void setHealthchecks(Map<String, Object> healthchecks) {
    this.healthchecks = healthchecks;
  }

  public Integer getSlots() {
    return slots;
  }

  public void setSlots(Integer slots) {
    this.slots = slots;
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
