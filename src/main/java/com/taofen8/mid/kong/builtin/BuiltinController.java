/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.builtin;

import com.taofen8.mid.kong.builtin.meta.VersionInfo;
import com.taofen8.mid.kong.builtin.monitor.ServiceMonitor;
import com.taofen8.mid.kong.builtin.responses.HiResponse;
import com.taofen8.mid.kong.builtin.responses.StatusResponse;
import com.taofen8.mid.kong.builtin.responses.VersionResponse;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class BuiltinController {

  private ServiceMonitor monitor = ServiceMonitor.getMonitorInstance();

  @BuiltinServiceMapping(path = "/__builtin/hi")
  public HiResponse hi(Map<String, Object> queryMap) {
    HiResponse hiResponse = new HiResponse();
    hiResponse.setSay("hi");
    return hiResponse;
  }

  @BuiltinServiceMapping(path = "/__builtin/version")
  public VersionResponse version(Map<String, Object> queryMap) {
    VersionResponse versionResponse = new VersionResponse();
    versionResponse.setVersion(VersionInfo.getVersion());
    return versionResponse;
  }

  @BuiltinServiceMapping(path = "/__builtin/status")
  public StatusResponse status(Map<String, Object> queryMap) {
    StatusResponse statusResponse = new StatusResponse();
    statusResponse.setStartAt(toDateString(monitor.getStartAt()));
    statusResponse.setAlive(betweenDateString(monitor.getStartAt(), System.currentTimeMillis()));
    statusResponse.setProcessRequests(monitor.getRequestCount().longValue());
    return statusResponse;
  }

  @BuiltinServiceMapping(path = "/__builtin/metrics", isJSONContextType = false)
  public String metrics(Map<String, Object> queryMap) throws IOException {
    StringWriter writer = new StringWriter();
    TextFormat.write004(writer, CollectorRegistry.defaultRegistry.metricFamilySamples());
    return writer.toString();
  }

  private String toDateString(long ms) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date(ms);
    return sdf.format(date);
  }

  private String betweenDateString(long start, long end) {
    long alive = (end - start) / 1000;
    StringBuilder builder = new StringBuilder();
    int[] divisors = {
        60 * 60 * 24,
        60 * 60,
        60,
        1
    };

    String[] unit = {
        "day",
        "hour",
        "min",
        "sec"
    };

    int idx = 0;
    while (idx < divisors.length) {
      long counts = alive / divisors[idx];
      if (counts > 0) {
        builder.append(counts + " " + (counts == 1 ? unit[idx] : unit[idx] + "s"));
      }

      alive = alive % divisors[idx];

      if (alive == 0) {
        break;
      }
      if (counts > 0) {
        builder.append(" ");
      }
      idx++;
    }

    return builder.toString();
  }
}
