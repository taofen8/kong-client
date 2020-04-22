/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin.request;


import java.util.HashMap;
import java.util.Map;

public class SNIReq extends BaseReq {

  final String name;
  final Map<String, String> certificate;


  public String getName() {
    return name;
  }

  public Map<String, String> getCertificate() {
    return certificate;
  }

  public SNIReq(Builder builder) {
    this.name = builder.name;
    this.certificate = builder.certificate;
  }


  public static class Builder {

    private String name;
    private Map<String, String> certificate;

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder certificateId(String certificateId) {
      if (this.certificate == null) {
        this.certificate = new HashMap<String, String>();
      }
      this.certificate.put("id", certificateId);
      return this;
    }

    public SNIReq bulid() {
      return new SNIReq(this);
    }
  }
}
