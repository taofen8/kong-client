/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin.response;


import com.taofen8.mid.kong.util.JsonUtil;
import java.util.List;

public class ServiceResp extends BaseResp {

  private String id;
  private Long created_at;
  private Long updated_at;
  private Integer connect_timeout;
  private String protocol;
  private String host;
  private Integer port;
  private String path;
  private String name;
  private Integer retries;
  private Integer read_timeout;
  private Integer write_timeout;
  private Integer qps;
  private List<String> tags;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Long getCreated_at() {
    return created_at;
  }

  public void setCreated_at(Long created_at) {
    this.created_at = created_at;
  }

  public Long getUpdated_at() {
    return updated_at;
  }

  public void setUpdated_at(Long updated_at) {
    this.updated_at = updated_at;
  }

  public Integer getConnect_timeout() {
    return connect_timeout;
  }

  public void setConnect_timeout(Integer connect_timeout) {
    this.connect_timeout = connect_timeout;
  }

  public String getProtocol() {
    return protocol;
  }

  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getRetries() {
    return retries;
  }

  public void setRetries(Integer retries) {
    this.retries = retries;
  }

  public Integer getRead_timeout() {
    return read_timeout;
  }

  public void setRead_timeout(Integer read_timeout) {
    this.read_timeout = read_timeout;
  }

  public Integer getWrite_timeout() {
    return write_timeout;
  }

  public void setWrite_timeout(Integer write_timeout) {
    this.write_timeout = write_timeout;
  }

  public Integer getQps() {
    return qps;
  }

  public void setQps(Integer qps) {
    this.qps = qps;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
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
