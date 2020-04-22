/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin.request;


import java.util.List;

public class ConsumerReq extends BaseReq {

  final String username;
  final String custom_id;
  final List<String> tags;

  public ConsumerReq(Builder builder) {
    this.username = builder.username;
    this.custom_id = builder.custom_id;
    this.tags = builder.tags;
  }

  public String getUsername() {
    return username;
  }

  public String getCustom_id() {
    return custom_id;
  }

  public List<String> getTags() {
    return tags;
  }

  public static class Builder {

    private String username;
    private String custom_id;
    private List<String> tags;

    public Builder username(String username) {
      this.username = username;
      return this;
    }

    public Builder customId(String customId) {
      this.custom_id = customId;
      return this;
    }

    public Builder tags(List<String> tags) {
      this.tags = tags;
      return this;
    }

    public ConsumerReq build() {
      return new ConsumerReq(this);
    }
  }
}
