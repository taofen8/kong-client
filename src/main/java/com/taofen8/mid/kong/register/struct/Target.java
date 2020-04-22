/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.register.struct;

import java.util.List;

public class Target {

  final String target;
  final Integer weight;
  final List<String> tags;

  public String target() {
    return target;
  }

  public Integer weight() {
    return weight;
  }

  public List<String> tags() {
    return tags;
  }

  public Target(Builder builder) {
    this.target = builder.target;
    this.weight = builder.weight;
    this.tags = builder.tags;
  }

  public static class Builder {

    private String target;
    private Integer weight = 100;
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

    public Target build() {
      this.weight = this.weight == 100 ? 100 : (Math.min(1000, Math.max(0, this.weight)));
      return new Target(this);
    }
  }
}
