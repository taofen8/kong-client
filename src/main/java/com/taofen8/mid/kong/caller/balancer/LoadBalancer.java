/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.caller.balancer;


import com.taofen8.mid.kong.caller.exception.LoadBalancerException;

public interface LoadBalancer {

  String nextNodeUrl() throws LoadBalancerException;

  void loadConfig(String balancerNodesConfig) throws LoadBalancerException;
}
