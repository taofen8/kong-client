/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin.response;


import java.util.List;

public class CertificateResp extends BaseResp {

  private String id;
  private String cert;
  private String key;
  private List<String> snis;
  private List<String> tags;
  private Long created_at;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCert() {
    return cert;
  }

  public void setCert(String cert) {
    this.cert = cert;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public List<String> getSnis() {
    return snis;
  }

  public void setSnis(List<String> snis) {
    this.snis = snis;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public Long getCreated_at() {
    return created_at;
  }

  public void setCreated_at(Long created_at) {
    this.created_at = created_at;
  }
}
