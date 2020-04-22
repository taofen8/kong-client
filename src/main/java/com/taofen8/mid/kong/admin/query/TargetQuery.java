/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin.query;

public class TargetQuery {

  final String id;
  final String target;
  final Integer weight;
  final Integer size;
  final String offset;

  public String getId() {
    return id;
  }

  public String getTarget() {
    return target;
  }

  public Integer getWeight() {
    return weight;
  }

  public Integer getSize() {
    return size;
  }

  public String getOffset() {
    return offset;
  }

  public TargetQuery(Builder builder) {
    this.id = builder.id;
    this.target = builder.target;
    this.weight = builder.weight;
    this.size = builder.size;
    this.offset = builder.offset;
  }

  public static class Builder {

    private String id;
    private String target;
    private Integer weight;
    private Integer size = 100;
    private String offset;

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder target(String target) {
      this.target = target;
      return this;
    }

    public Builder weight(Integer weight) {
      this.weight = weight;
      return this;
    }

    public Builder size(Integer size) {
      this.size = size;
      return this;
    }

    public Builder offset(String offset) {
      this.offset = offset;
      return this;
    }

    public TargetQuery build() {
      return new TargetQuery(this);
    }
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder("?size=" + this.size);
    if (this.id != null) {
      builder.append("&id=" + this.id);
    }
    if (this.target != null) {
      builder.append("&target=" + this.target);
    }
    if (this.weight != null) {
      builder.append("&weight=" + this.weight);
    }
    if (this.offset != null) {
      builder.append("&offset=" + this.offset);
    }

    return builder.toString();
  }
}
