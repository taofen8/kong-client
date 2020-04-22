/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.builtin.monitor;

import java.util.concurrent.atomic.AtomicLong;

/**
 *
 */
public final class ServiceMonitor {

  //服务器启动时间
  private long startAt;
  //请求计数
  private AtomicLong requestCount = new AtomicLong(0);
  private volatile static ServiceMonitor monitor = null;

  public AtomicLong getRequestCount() {
    return requestCount;
  }

  public long getStartAt() {
    return startAt;
  }

  private ServiceMonitor() {
  }

  public static ServiceMonitor getMonitorInstance() {
    if (monitor == null) {
      synchronized (ServiceMonitor.class) {
        if (monitor == null) {
          monitor = new ServiceMonitor();
        }
      }
    }
    return monitor;
  }


  //请求计数
  public void incrRequestCount() {
    this.requestCount.incrementAndGet();
  }

  //服务器启动
  public void start() {
    this.startAt = System.currentTimeMillis();
  }

  








}
