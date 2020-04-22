/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.caller.balancer;

import com.taofen8.mid.kong.caller.exception.LoadBalancerException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class WeightedRoundRobinBalancer implements LoadBalancer {

  Logger logger = LoggerFactory.getLogger(WeightedRoundRobinBalancer.class);

  List<TargetHost> hostList = new ArrayList<TargetHost>();
  private String configString;

  @Override
  public String nextNodeUrl() throws LoadBalancerException {
    if (this.hostList.size() == 0) {
      throw new LoadBalancerException(this.getClass().getName() + "balancer is broken");
    }

    int totalWeight = 0;
    for (TargetHost eachHost : this.hostList) {
      totalWeight += eachHost.getEffectiveWeight();
    }

    TargetHost picked = this.hostList.get(0);
    picked.currentWeight += picked.getEffectiveWeight();
    for (int i = 1; i < this.hostList.size(); ++i) {
      this.hostList.get(i).currentWeight += this.hostList.get(i).getEffectiveWeight();
      if (picked.currentWeight < this.hostList.get(i).currentWeight) {
        picked = this.hostList.get(i);
      }
    }
    picked.currentWeight -= totalWeight;
    return picked.toString();
  }

  /**
   * sample config string 192.168.0.1:8000 w:200,192.168.0.2:8000 w:100
   * @param balancerNodesConfig
   * @throws LoadBalancerException
   */
  @Override
  public void loadConfig(String balancerNodesConfig) throws LoadBalancerException {
    if (CollectionUtils.isEmpty(this.hostList)) {
      if (StringUtils.isEmpty(balancerNodesConfig)) {
        throw new LoadBalancerException(
            this.getClass().getName() + ",loadConfig fail,parameter balancerUrlsConfig is emtpy");
      }
      this.hostList = this.resolveFromConfig(balancerNodesConfig);
      this.configString = balancerNodesConfig;
    } else {
      List rollbackHostList = null;
      if (null != balancerNodesConfig && !this.configString.equals(balancerNodesConfig)) {
        rollbackHostList = new ArrayList();
        for (TargetHost each : this.hostList) {
          try {
            rollbackHostList.add(each.clone());
          } catch (CloneNotSupportedException e) {
            logger.error("CloneNotSupportedException error ", e);
          }
        }

        this.hostList = this.resolveFromConfig(balancerNodesConfig);
        if (CollectionUtils.isEmpty(this.hostList)) {
          this.hostList = rollbackHostList;
        } else {
          this.configString = balancerNodesConfig;
        }
      }
    }
  }

  /**
   * format: case 1:192.168.0.1:8000 case 2:192.168.0.1:8000 w:100,192.168.0.2:8000 w:200
   */
  private List<TargetHost> resolveFromConfig(String configString) {
    List<TargetHost> list = new ArrayList<TargetHost>(8);
    String[] hosts = configString.split(",");
    for (String host : hosts) {
      String[] hostWithWeight = host.split(" ");
      String[] ipWithPort = hostWithWeight[0].split(":");
      String weight = "100";
      if (hostWithWeight.length == 2) {
        weight = hostWithWeight[1].split(":")[1];
      }
      list.add(
          new TargetHost.Builder()
              .ip(ipWithPort[0])
              .port(ipWithPort[1])
              .weight(Integer.valueOf(weight))
              .build()
      );
    }
    return list;
  }
}
