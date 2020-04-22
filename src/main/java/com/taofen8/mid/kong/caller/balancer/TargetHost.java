/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.caller.balancer;

public class TargetHost implements Cloneable, Comparable<TargetHost> {

  private final String ip;
  private final String port;
  private final int weight;
  private final int effectiveWeight;
  int currentWeight;

  public void setCurrentWeight(int currentWeight) {
    this.currentWeight = currentWeight;
  }

  public String getIp() {
    return ip;
  }

  public String getPort() {
    return port;
  }

  public int getWeight() {
    return weight;
  }

  public int getEffectiveWeight() {
    return effectiveWeight;
  }

  public int getCurrentWeight() {
    return currentWeight;
  }

  public TargetHost(TargetHost.Builder builder) {
    this.ip = builder.ip;
    this.port = builder.port;
    this.weight = builder.weight;
    this.effectiveWeight = builder.weight;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("http://");
    builder.append(ip);
    builder.append(":");
    builder.append(port);
    return builder.toString();
  }


  //sort by weight desc
  public int compareTo(TargetHost o) {
    return o.weight - this.weight;
  }

  public static class Builder {

    private String ip;
    private String port;
    private int weight;
    private int effectiveWeight;

    public Builder ip(String ip) {
      this.ip = ip;
      return this;
    }

    public Builder port(String port) {
      this.port = port;
      return this;
    }

    public Builder weight(int weight) {
      this.weight = weight;
      return this;
    }

    public Builder effectiveWeight(int effectiveWeight) {
      this.effectiveWeight = effectiveWeight;
      return this;
    }

    public TargetHost build() {
      return new TargetHost(this);
    }
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    super.clone();
    TargetHost copy = new TargetHost.Builder()
        .ip(new String(this.ip))
        .port(new String(this.port))
        .weight(this.weight)
        .effectiveWeight(this.effectiveWeight)
        .build();
    copy.currentWeight = this.currentWeight;

    return copy;
  }


}
