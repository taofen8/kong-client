/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.builtin.responses;

public class StatusResponse extends BaseResponse {

  private String startAt;
  private String alive;
  private long processRequests;

  public String getStartAt() {
    return startAt;
  }

  public void setStartAt(String startAt) {
    this.startAt = startAt;
  }

  public String getAlive() {
    return alive;
  }

  public void setAlive(String alive) {
    this.alive = alive;
  }

  public long getProcessRequests() {
    return processRequests;
  }

  public void setProcessRequests(long processRequests) {
    this.processRequests = processRequests;
  }
}
