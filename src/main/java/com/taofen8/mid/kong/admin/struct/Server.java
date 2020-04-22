/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin.struct;

public class Server {

  private String total_requests;
  private String connections_active;
  private String connections_accepted;
  private String connections_handled;
  private String connections_reading;
  private String connections_writing;
  private String connections_waiting;

  public String getTotal_requests() {
    return total_requests;
  }

  public void setTotal_requests(String total_requests) {
    this.total_requests = total_requests;
  }

  public String getConnections_active() {
    return connections_active;
  }

  public void setConnections_active(String connections_active) {
    this.connections_active = connections_active;
  }

  public String getConnections_accepted() {
    return connections_accepted;
  }

  public void setConnections_accepted(String connections_accepted) {
    this.connections_accepted = connections_accepted;
  }

  public String getConnections_handled() {
    return connections_handled;
  }

  public void setConnections_handled(String connections_handled) {
    this.connections_handled = connections_handled;
  }

  public String getConnections_reading() {
    return connections_reading;
  }

  public void setConnections_reading(String connections_reading) {
    this.connections_reading = connections_reading;
  }

  public String getConnections_writing() {
    return connections_writing;
  }

  public void setConnections_writing(String connections_writing) {
    this.connections_writing = connections_writing;
  }

  public String getConnections_waiting() {
    return connections_waiting;
  }

  public void setConnections_waiting(String connections_waiting) {
    this.connections_waiting = connections_waiting;
  }
}
