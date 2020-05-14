/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.builtin.collector;


import io.prometheus.client.Counter;
import org.springframework.util.StringUtils;

public class CounterCollector {

  private Counter counter;

  private String[] defaultLabelValues;

  private CounterCollector() {
  }


  public void inc(double value) {
    if (this.defaultLabelValues != null || defaultLabelValues.length > 0) {
      counter.labels(defaultLabelValues).inc(value);
    } else {
      counter.inc(value);
    }
  }

  public void inc() {
    this.inc(1d);
  }

  public void inc(double value, String... labelValues) {
    counter.labels(labelValues).inc(value);
  }

  public void inc(String... labelValues) {
    this.inc(1d, labelValues);
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
        throw new IllegalArgumentException("CounterCollector param illegal");
      }
      this.help = help;
      this.name = name;
    }

    public Builder labelNames(String... labelNames) {
      this.labelNames = labelNames;
      return this;
    }

    public Builder labelValues(String... labelValues) {
      this.labelValues = labelValues;
      return this;
    }

    public CounterCollector build() {
      Counter counter = Counter.build(this.name, this.help)
          .labelNames(this.labelNames).create().register();

      if(this.labelValues != null && labelValues.length > 0){
        counter.labels(labelValues);
      }
      CounterCollector collector = new CounterCollector();
      collector.counter = counter;
      collector.defaultLabelValues = this.labelValues;
      return collector;
    }
  }
}
