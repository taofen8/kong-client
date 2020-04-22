/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin.request;


import java.util.List;

public class CertificateReq extends BaseReq {

  final String cert;
  final String key;
  final List<String> tags;
  final List<String> snis;

  public String getCert() {
    return cert;
  }

  public String getKey() {
    return key;
  }

  public List<String> getTags() {
    return tags;
  }

  public List<String> getSnis() {
    return snis;
  }

  public CertificateReq(Builder builder) {
    this.cert = builder.cert;
    this.key = builder.key;
    this.snis = builder.snis;
    this.tags = builder.tags;
  }

  public static class Builder {

    private String cert;
    private String key;
    private List<String> tags;
    private List<String> snis;

    public Builder cert(String cert) {
      this.cert = cert;
      return this;
    }

    public Builder key(String key) {
      this.key = key;
      return this;
    }

    public Builder snis(List<String> snis) {
      this.snis = snis;
      return this;
    }

    public Builder tags(List<String> tags) {
      this.tags = tags;
      return this;
    }

    public CertificateReq build() {
      return new CertificateReq(this);
    }
  }
}
