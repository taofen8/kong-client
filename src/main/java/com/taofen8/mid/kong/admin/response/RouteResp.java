/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin.response;


import com.taofen8.mid.kong.util.JsonUtil;
import java.util.List;
import java.util.Map;

public class RouteResp extends BaseResp {

  private String id;
  private String updated_at;
  private List<String> protocols;
  private List<String> methods;
  private List<String> hosts;
  private List<String> paths;
  private Integer regex_priority;
  private Boolean strip_path;
  private Boolean preserve_host;
  private String path_handling;
  private Map<String, String> service;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUpdated_at() {
    return updated_at;
  }

  public void setUpdated_at(String updated_at) {
    this.updated_at = updated_at;
  }

  public List<String> getProtocols() {
    return protocols;
  }

  public void setProtocols(List<String> protocols) {
    this.protocols = protocols;
  }

  public List<String> getMethods() {
    return methods;
  }

  public void setMethods(List<String> methods) {
    this.methods = methods;
  }

  public List<String> getHosts() {
    return hosts;
  }

  public void setHosts(List<String> hosts) {
    this.hosts = hosts;
  }

  public List<String> getPaths() {
    return paths;
  }

  public void setPaths(List<String> paths) {
    this.paths = paths;
  }

  public Integer getRegex_priority() {
    return regex_priority;
  }

  public void setRegex_priority(Integer regex_priority) {
    this.regex_priority = regex_priority;
  }

  public Boolean getStrip_path() {
    return strip_path;
  }

  public void setStrip_path(Boolean strip_path) {
    this.strip_path = strip_path;
  }

  public Boolean getPreserve_host() {
    return preserve_host;
  }

  public void setPreserve_host(Boolean preserve_host) {
    this.preserve_host = preserve_host;
  }

  public String getPath_handling() {
    return path_handling;
  }

  public void setPath_handling(String path_handling) {
    this.path_handling = path_handling;
  }

  public Map<String, String> getService() {
    return service;
  }

  public void setService(Map<String, String> service) {
    this.service = service;
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
