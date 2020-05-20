/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.caller;

import com.taofen8.mid.kong.caller.balancer.WeightedRoundRobinBalancer;
import com.taofen8.mid.kong.caller.exception.LoadBalancerException;
import com.taofen8.mid.kong.config.Config.ConfigEntry;
import com.taofen8.mid.kong.config.ConfigProvider;
import org.junit.Test;

public class LoadBalancerTest {

  @Test
  public void testCircutBreaker() throws LoadBalancerException {
    String config = "192.168.0.1:8000 w:100,192.168.0.2:8000 w:50";

    CocoClient cocoClient = new CocoClient.Builder()
        .specifiedBalancerNodesConfig(config)
        .balancer(new WeightedRoundRobinBalancer()).build();

    CocoClient cocoClient2 = new CocoClient.Builder()
        .specifiedBalancerNodesConfig(config)
        .balancer(new WeightedRoundRobinBalancer()).build();

    int count = 0;
    while (count < 10000) {
      System.out.println("nextNode," + cocoClient.getNextNode());
      count++;

      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }


  @Test
  public void testHackWebContextBalancer() throws LoadBalancerException {
    String config = "192.168.0.1:8000 w:500,192.168.0.2:8000 w:300,192.168.0.3:9876 w:1000";
    ConfigProvider.getConfig().configure(ConfigEntry.KONG_CALLER_BALANCER_NODES, config);

    CocoClient cocoClient = new CocoClient.Builder()
        .balancer(new WeightedRoundRobinBalancer()).build();

    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          Thread.sleep(10000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        String conf = "192.168.0.4:8000 w:300,192.168.0.5:8000 w:200";
        ConfigProvider.getConfig().configure(ConfigEntry.KONG_CALLER_BALANCER_NODES, conf);
      }
    }).start();

    int count = 0;
    while (count < 10000) {
      System.out.println("nextNode," + cocoClient.getNextNode());
      count++;

      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }


  }


  @Test
  public void testWeightedRoundRobin() throws LoadBalancerException {
    String conf = "192.168.0.1:8000 w:500,192.168.0.2:8000 w:300,192.168.0.3:8000 w:200";

    final WeightedRoundRobinBalancer balancer = new WeightedRoundRobinBalancer();
    balancer.loadConfig(conf);

    int count = 0;
    while (count < 10000) {
      System.out.println(balancer.nextNodeUrl().toString());
      count++;

      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

  }
}
