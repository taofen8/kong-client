/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.caller.balancer;

import com.taofen8.mid.kong.caller.balancer.LoadBalancer.KongServer;

public class WeightedServer extends KongServer implements Cloneable, Comparable<WeightedServer> {

  private int weight;
  private int currentWeight;

  public void setCurrentWeight(int currentWeight) {
    this.currentWeight = currentWeight;
  }

  public int getWeight() {
    return weight;
  }

  public int getCurrentWeight() {
    return currentWeight;
  }

  public WeightedServer(String ip, int port, int weight) {
    super(ip, port);
    this.weight = weight;
  }


  //sort by weight desc
  public int compareTo(WeightedServer o) {
    return o.weight - this.weight;
  }


  @Override
  public Object clone() throws CloneNotSupportedException {
    super.clone();
    WeightedServer copy = new WeightedServer(this.getIp(), this.getPort(), this.getWeight());
    copy.currentWeight = this.getCurrentWeight();

    return copy;
  }

}
