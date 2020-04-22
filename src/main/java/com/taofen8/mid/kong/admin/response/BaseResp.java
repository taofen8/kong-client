/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin.response;

public class BaseResp {

  protected transient Integer status = 200;

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setSchema(String schema) {
    this.schema = schema;
  }

  protected transient String schema = "http";

  public Boolean succeed() {
    return this.status >= 200 && this.status < 300;
  }

  public Boolean succeedOrIngoreConflict() {
    return succeed() || this.status == 409;
  }

  public static class Schema {

    private Schema() {
    }

    public final static String HTTP = "http";
    public final static String HTTPS = "https";
  }

  public static BaseResp noContent() {
    BaseResp resp = new BaseResp();
    resp.status = 204;
    resp.schema = "HTTP";
    return resp;
  }

  protected String statusDesc() {
    switch (this.status) {
      case 200:
        return "OK";
      case 201:
        return "Created";
      case 204:
        return "No Content";
      case 400:
        return "Bad Request";
      case 404:
        return "Not Found";
      case 403:
        return "Forbidden";
      case 409:
        return "Conflict";
      case 500:
        return "Internal Server Error";
      default:
        return "Unknown";
    }
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(this.schema.toUpperCase());
    builder.append(" ");
    builder.append(this.status);
    builder.append(" ");
    builder.append(statusDesc());
    return builder.toString();
  }
}
