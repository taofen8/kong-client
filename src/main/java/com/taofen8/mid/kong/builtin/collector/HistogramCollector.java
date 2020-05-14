/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.builtin.collector;

import io.prometheus.client.Histogram;
import org.springframework.util.StringUtils;

public class HistogramCollector {

  private Histogram histogram;

  private String[] defaultLabelValues;

  private HistogramCollector() {
  }


  public void observe(double value) {
    if (this.defaultLabelValues != null || defaultLabelValues.length > 0) {
      this.histogram.labels(defaultLabelValues).observe(value);
    } else {
      this.histogram.observe(value);
    }
  }

  public void observe(double value, String... labelValues) {
    this.histogram.labels(labelValues).observe(value);
  }


  public static class Builder {

    private String help;
    private String name;

    private String[] labelNames;
    private String[] labelValues;
    private double[] buckets;

    /**
     * @param name 指标名称,非空
     * @param help 指标说明，描述该指标的含义，非空
     **/
    public Builder(String name, String help) {
      if (StringUtils.isEmpty(name) || StringUtils.isEmpty(help)) {
        throw new IllegalArgumentException("HistogramCollector param illegal");
      }
      this.help = help;
      this.name = name;
    }

    public HistogramCollector.Builder buckets(double... buckets) {
      this.buckets = buckets;
      return this;
    }

    public HistogramCollector.Builder labelNames(String... labelNames) {
      this.labelNames = labelNames;
      return this;
    }

    public HistogramCollector.Builder labelValues(String... labelValues) {
      this.labelValues = labelValues;
      return this;
    }


    public HistogramCollector build() {
      Histogram histogram = Histogram.build(this.name, this.help).labelNames(labelNames)
          .buckets(this.buckets).create().register();

      if (this.labelValues != null && labelValues.length > 0) {
        histogram.labels(this.labelValues);
      }

      HistogramCollector collector = new HistogramCollector();
      collector.histogram = histogram;
      collector.defaultLabelValues = this.labelValues;
      return collector;
    }
  }


}
