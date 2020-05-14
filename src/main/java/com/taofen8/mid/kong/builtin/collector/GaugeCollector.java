/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.builtin.collector;

import io.prometheus.client.Gauge;
import org.springframework.util.StringUtils;

public class GaugeCollector {


  private Gauge gauge;

  private String[] defaultLabelValues;

  private GaugeCollector() {
  }


  public void inc(double value, String... labelValues) {
    gauge.labels(labelValues).inc(value);
  }

  public void inc(String... labelValues) {
    this.inc(1d, labelValues);
  }

  public void inc() {
    this.inc(1d);
  }

  public void inc(double value) {
    if (this.defaultLabelValues != null || defaultLabelValues.length > 0) {
      gauge.labels(defaultLabelValues).inc(value);
    } else {
      gauge.inc(value);
    }
  }


  public void dec(double value, String... labelValues) {
    gauge.labels(labelValues).dec(value);
  }

  public void dec(String... labelValues) {
    this.dec(1d, labelValues);
  }

  public void dec() {
    this.dec(1d);
  }

  public void dec(double value) {
    if (this.defaultLabelValues != null || defaultLabelValues.length > 0) {
      gauge.labels(defaultLabelValues).dec(value);
    } else {
      gauge.dec(value);
    }
  }

  public void set(double value, String... labelValues) {
    gauge.labels(labelValues).set(value);
  }

  public void set(double value) {
    if (this.defaultLabelValues != null || defaultLabelValues.length > 0) {
      gauge.labels(defaultLabelValues).set(value);
    } else {
      gauge.set(value);
    }
  }

  public static class Builder {

    private String help;
    private String name;
    private String[] labelNames;
    private String[] labelValues;

    /**
     * @param name 指标名称，以下划线隔开 ,非空
     * @param help 指标说明，描述该指标的含义，非空
     **/
    public Builder(String name, String help) {
      if (StringUtils.isEmpty(name) || StringUtils.isEmpty(help)) {
        throw new IllegalArgumentException("GaugeCollector param illegal");
      }
      this.help = help;
      this.name = name;
    }

    public GaugeCollector.Builder labelNames(String... labelNames) {
      this.labelNames = labelNames;
      return this;
    }

    public GaugeCollector.Builder labelValues(String... labelValues) {
      this.labelValues = labelValues;
      return this;
    }


    public GaugeCollector build() {
      Gauge gauge = Gauge.build(this.name, this.help)
          .labelNames(this.labelNames).create().register();
      if(this.labelValues != null && labelValues.length > 0){
        gauge.labels(labelValues);
      }

      GaugeCollector collector = new GaugeCollector();
      collector.gauge = gauge;
      collector.defaultLabelValues = this.labelValues;
      return collector;
    }
  }
}
