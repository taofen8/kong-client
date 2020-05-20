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

public class WeightedRoundRobinBalancer implements LoadBalancer<WeightedServer> {

  private Logger logger = LoggerFactory.getLogger(WeightedRoundRobinBalancer.class);

  private List<WeightedServer> serverList = new ArrayList<WeightedServer>();
  private String configString;

  @Override
  public String nextNodeUrl() throws LoadBalancerException {
    List<WeightedServer> healthyServerList = getHealthyServerList();

    int totalWeight = 0;
    for (WeightedServer eachHost : healthyServerList) {
      totalWeight += eachHost.getWeight();
    }

    WeightedServer picked = healthyServerList.get(0);
    picked.setCurrentWeight(picked.getCurrentWeight() + picked.getWeight());

    for (int i = 1; i < healthyServerList.size(); i++) {
      WeightedServer server = healthyServerList.get(i);
      server.setCurrentWeight(server.getCurrentWeight() + server.getWeight());
      if (picked.getCurrentWeight() < server.getCurrentWeight()) {
        picked = server;
      }
    }

    picked.setCurrentWeight(picked.getCurrentWeight() - totalWeight);
    return picked.toString();
  }

  private List<WeightedServer> getHealthyServerList() throws LoadBalancerException {
    if (this.serverList.size() == 0) {
      throw new LoadBalancerException(this.getClass().getName() + "balancer is broken");
    }

    List<WeightedServer> healthyServerList = new ArrayList<WeightedServer>();
    for (WeightedServer server : serverList) {
      if (server.isAlive()) {
        healthyServerList.add(server);
      }
    }
    if (healthyServerList.size() == 0) {
      throw new LoadBalancerException(this.getClass().getName() + "balancer is broken");
    }
    return healthyServerList;
  }

  /**
   * sample config string 192.168.0.1:8000 w:200,192.168.0.2:8000 w:100
   *
   * @param balancerNodesConfig
   * @throws LoadBalancerException
   */
  @Override
  public void loadConfig(String balancerNodesConfig) throws LoadBalancerException {
    if (CollectionUtils.isEmpty(this.serverList)) {
      if (StringUtils.isEmpty(balancerNodesConfig)) {
        throw new LoadBalancerException(
            this.getClass().getName() + ",loadConfig fail,parameter balancerUrlsConfig is emtpy");
      }
      this.serverList = this.resolveFromConfig(balancerNodesConfig);
      this.configString = balancerNodesConfig;
    } else {
      List rollbackHostList = null;
      if (null != balancerNodesConfig && !this.configString.equals(balancerNodesConfig)) {
        rollbackHostList = new ArrayList();
        for (WeightedServer each : this.serverList) {
          try {
            rollbackHostList.add(each.clone());
          } catch (CloneNotSupportedException e) {
            logger.error("CloneNotSupportedException error ", e);
          }
        }

        this.serverList = this.resolveFromConfig(balancerNodesConfig);
        if (CollectionUtils.isEmpty(this.serverList)) {
          this.serverList = rollbackHostList;
        } else {
          this.configString = balancerNodesConfig;
        }
      }
    }
  }

  @Override
  public List<WeightedServer> getServerList() {
    return serverList;
  }

  /**
   * format: case 1:192.168.0.1:8000 case 2:192.168.0.1:8000 w:100,192.168.0.2:8000 w:200
   */
  private List<WeightedServer> resolveFromConfig(String configString) {
    List<WeightedServer> list = new ArrayList<WeightedServer>(8);
    String[] hosts = configString.split(",");
    for (String host : hosts) {
      String[] hostWithWeight = host.split(" ");
      String[] ipWithPort = hostWithWeight[0].split(":");
      String weight = "100";
      if (hostWithWeight.length == 2) {
        weight = hostWithWeight[1].split(":")[1];
      }
      list.add(
          new WeightedServer(ipWithPort[0], Integer.valueOf(ipWithPort[1]), Integer.valueOf(weight))
      );
    }
    return list;
  }
}
