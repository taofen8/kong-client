/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.register.support;

import java.lang.management.ManagementFactory;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.regex.Pattern;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class EnvHelper {

  private static Logger logger = LoggerFactory.getLogger(EnvHelper.class);
  private static final String DEFAULT_IP = "127.0.0.1";

  private EnvHelper() {
  }

  public static String getLocalIP() {
    try {
      String ip = System.getenv("HOST_IP");
      if (StringUtils.isEmpty(ip)) {
        List<String> ipList = getOrderIpList();

        if (ipList.size() > 0) {
          ip = getOrderIpList().get(0);
        } else {
          ip = DEFAULT_IP;
        }
      }
      return ip;
    } catch (Exception e) {
      return DEFAULT_IP;
    }
  }

  private static List<String> getOrderIpList() {
    List<String> ipList = getIpList();
    PriorityQueue<PriorityIp> queue = new PriorityQueue<PriorityIp>();
    for (String ip : ipList) {
      queue.add(getPriortyIp(ip));
    }
    ipList = new ArrayList<String>();
    for (PriorityIp pi : queue) {
      if (pi.getPriority() > 0) {
        ipList.add(pi.getIp());
      }
    }
    return ipList;
  }

  private static List<String> getIpList() {
    List<String> ipList = new ArrayList<String>();

    try {
      Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
      NetworkInterface networkInterface;
      Enumeration<InetAddress> inetAddresses;
      InetAddress inetAddress;
      String ip;
      while (networkInterfaces.hasMoreElements()) {
        networkInterface = networkInterfaces.nextElement();
        inetAddresses = networkInterface.getInetAddresses();
        while (inetAddresses.hasMoreElements()) {
          inetAddress = inetAddresses.nextElement();
          if (inetAddress != null && inetAddress instanceof Inet4Address) { // IPV4
            ip = inetAddress.getHostAddress();
            ipList.add(ip);
          }
        }
      }
    } catch (SocketException e) {
      logger.error("error by:", e);
    }
    return ipList;
  }

  private static PriorityIp getPriortyIp(String ip) {
    int priority = 0;
    if (DEFAULT_IP.equals(ip)) {
      priority = 0;
    } else if (Pattern.compile("192\\.168.*").matcher(ip).matches()) {
      priority = 9;
    } else if (Pattern.compile("172\\.16.*").matcher(ip).matches()) {
      priority = 8;
    } else if (Pattern.compile("169\\.254.*").matcher(ip).matches()) {
      priority = 7;
    } else if (Pattern.compile("10\\..*").matcher(ip).matches()) {
      priority = -1;
    } else {
      priority = 5;
    }
    return new PriorityIp(priority, ip);
  }

  static class PriorityIp implements Comparable<PriorityIp> {

    private int priority;
    private String ip;

    public PriorityIp(int priority, String ip) {
      this.priority = priority;
      this.ip = ip;
    }

    public int getPriority() {
      return priority;
    }

    public String getIp() {
      return ip;
    }

    public int compareTo(PriorityIp o) {
      return o.priority - this.priority;
    }
  }

  public static int getTomcatPort() {
    try {
      String port = System.getenv("HOST_PORT");

      if (StringUtils.isEmpty(port)) {
        MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
        Set<ObjectName> objectNames = beanServer.queryNames(new ObjectName("*:type=Connector,*"),
            Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
        port = objectNames.iterator().next().getKeyProperty("port");
      }
      return Integer.valueOf(port);
    } catch (Exception e) {
      return 8080;
    }
  }
}
