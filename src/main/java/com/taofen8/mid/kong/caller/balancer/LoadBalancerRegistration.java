/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.caller.balancer;

import com.taofen8.mid.kong.caller.balancer.LoadBalancer.KongServer;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadBalancerRegistration {

  private LoadBalancerRegistration() {
  }

  private static Logger logger = LoggerFactory.getLogger(LoadBalancerRegistration.class);
  private static volatile Set<LoadBalancer> balancers = new HashSet<LoadBalancer>();
  private static volatile Map<KongServer, ServerChecker> serversForCheck = new HashMap<>();
  private static OkHttpClient httpClient = new OkHttpClient.Builder()
      .connectTimeout(1, TimeUnit.SECONDS)
      .readTimeout(1, TimeUnit.SECONDS).writeTimeout(1, TimeUnit.SECONDS).build();
  private static String PING_PATH = "/__builtin/hi";
  private static long CHECKER_INTERVAL_MILLS = 1000;

  public synchronized static void register(LoadBalancer balancer) {
    if (balancer == null) {
      return;
    }
    balancers.add(balancer);

    List<KongServer> kongServers = balancer.getServerList();
    if (kongServers == null) {
      return;
    }
    for (KongServer server : kongServers) {
      if (serversForCheck.containsKey(server)) {
        continue;
      }

      final KongServer s = new KongServer(server.getIp(), server.getPort(), server.getSchema());
      ServerChecker t = new ServerChecker(server);
      t.start();
      serversForCheck.put(s, t);
    }

    Set<KongServer> totoalServers = new HashSet<>();
    for (LoadBalancer b : balancers) {
      for (Object kongServer : b.getServerList()) {
        totoalServers.add((KongServer) kongServer);
      }
    }

    //remove unused servers and stop checking
    Iterator<KongServer> serverIterator = serversForCheck.keySet().iterator();
    while (serverIterator.hasNext()) {
      KongServer checkingServer = serverIterator.next();
      if (!totoalServers.contains(checkingServer)) {
        ServerChecker t = serversForCheck.get(checkingServer);
        if (t != null) {
          t.setStop(true);
        }
        serverIterator.remove();
      }
    }

    if (logger.isDebugEnabled()) {
      for (KongServer server : serversForCheck.keySet()) {
        logger.debug("current effective kong server:" + server.toString());
      }
    }
  }

  private static class ServerChecker extends Thread {

    private volatile boolean stop = false;
    private KongServer server;

    public ServerChecker(KongServer server) {
      this.server = server;
    }

    public boolean isStop() {
      return stop;
    }

    public void setStop(boolean stop) {
      this.stop = stop;
    }

    public KongServer getServer() {
      return server;
    }

    public void setServer(KongServer server) {
      this.server = server;
    }

    @Override
    public void run() {
      while(!stop) {
        boolean isAlive = LoadBalancerRegistration.isAlive(server.toString());
        if (server.isAlive() != isAlive) {
          for (LoadBalancer balancer : balancers) {
            for (Object server1 : balancer.getServerList()) {
              if (server1.equals(server)) {
                ((KongServer) server1).setAlive(isAlive);
                server.setAlive(isAlive);
              }
            }
          }
        }
        try {
          Thread.sleep(CHECKER_INTERVAL_MILLS);
        } catch (InterruptedException e) {
          if (Thread.interrupted()) {
            Thread.currentThread().interrupt();
          }
        }
      }
    }
  }

  private static boolean isAlive(String url) {
    if (url == null) {
      return false;
    }

    if (url.endsWith("/")) {
      url = url.substring(0, url.length() - 1) + PING_PATH;
    } else {
      url += PING_PATH;
    }

    Request request = new Request.Builder().url(url)
        .addHeader("from", "kong-client-balancer-ping").build();

    int retry = 0;
    while (retry < 3) {

      try {
        Response response = httpClient.newCall(request).execute();
        if (response.isSuccessful()) {
          return true;
        } else {
          return false;
        }
      } catch (IOException e) {
        logger.error("pingurl error,{},{}", url, retry);
        retry++;
      } catch (Exception e) {
        logger.error("pingurl error,{},{}", url, retry, e);
        return false;
      }
    }
    return false;
  }


}
