/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin.request;


import java.util.List;

public class TargetReq extends BaseReq {

  final String target;
  final Integer weight;
  final List<String> tags;

  public String getTarget() {
    return target;
  }

  public Integer getWeight() {
    return weight;
  }

  public List<String> getTags() {
    return tags;
  }

  public TargetReq(Builder builder) {
    this.target = builder.target;
    this.weight = builder.weight;
    this.tags = builder.tags;
  }

  public static class Builder {

    private String target;
    private Integer weight;
    private List<String> tags;

    public Builder target(String target) {
      this.target = target;
      return this;
    }

    public Builder weight(Integer weight) {
      this.weight = weight;
      return this;
    }

    public Builder tags(List<String> tags) {
      this.tags = tags;
      return this;
    }


    public TargetReq build() {
      return new TargetReq(this);
    }
  }
}
