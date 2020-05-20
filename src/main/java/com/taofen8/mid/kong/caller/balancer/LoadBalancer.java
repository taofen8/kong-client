/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.caller.balancer;


import com.taofen8.mid.kong.caller.balancer.LoadBalancer.KongServer;
import com.taofen8.mid.kong.caller.exception.LoadBalancerException;
import java.util.List;
import java.util.Objects;

public interface LoadBalancer<T extends KongServer> {

  String nextNodeUrl() throws LoadBalancerException;

  void loadConfig(String balancerNodesConfig) throws LoadBalancerException;

  List<T> getServerList();

  class KongServer {

    private String ip;
    private int port = 8000;
    private String schema = "http";
    private boolean isAlive = true;

    public KongServer() {
    }

    public KongServer(String ip) {
      this.ip = ip;
    }

    public KongServer(String ip, int port) {
      this.ip = ip;
      this.port = port;
    }

    public KongServer(String ip, int port, String schema) {
      this.ip = ip;
      this.port = port;
      this.schema = schema;
    }

    public boolean isAlive() {
      return isAlive;
    }

    public void setAlive(boolean alive) {
      isAlive = alive;
    }

    public int getPort() {
      return port;
    }

    public void setPort(int port) {
      this.port = port;
    }

    public void setIp(String ip) {
      this.ip = ip;
    }

    public void setSchema(String schema) {
      this.schema = schema;
    }

    public String getIp() {
      return ip;
    }

    public String getSchema() {
      return schema;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null) {
        return false;
      }

      if (!(o instanceof KongServer)) {
        return false;
      }

      KongServer server = (KongServer) o;
      return Objects.equals(ip, server.ip) &&
          Objects.equals(port, server.port) &&
          Objects.equals(schema, server.schema);
    }

    @Override
    public int hashCode() {
      return Objects.hash(ip, port, schema);
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(this.getSchema());
      builder.append("://");
      builder.append(this.getIp());
      builder.append(":");
      builder.append(this.getPort());
      return builder.toString();
    }
  }
}
