/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin.response;


import com.taofen8.mid.kong.util.JsonUtil;

/**
 * Retrieve Node Response Body
 *
 * @author sheny taofen8.com
 * Have not filled params plugins and configuration
 */
public class RetrieveNodeInfoResp extends BaseResp {

  private String hostname;
  private String node_id;
  private String lua_version;
  private String tagline;
  private String version;

  public String getHostname() {
    return hostname;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  public String getNode_id() {
    return node_id;
  }

  public void setNode_id(String node_id) {
    this.node_id = node_id;
  }

  public String getLua_version() {
    return lua_version;
  }

  public void setLua_version(String lua_version) {
    this.lua_version = lua_version;
  }

  public String getTagline() {
    return tagline;
  }

  public void setTagline(String tagline) {
    this.tagline = tagline;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
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
