/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.caller;

import com.taofen8.mid.kong.caller.balancer.LoadBalancer;
import com.taofen8.mid.kong.caller.exception.LoadBalancerException;
import com.taofen8.mid.kong.codec.Codec;
import com.taofen8.mid.kong.config.ConfigProvider;
import com.taofen8.mid.kong.config.internal.InternalConfig.InternalConfigEntry;
import com.taofen8.mid.kong.caller.balancer.WeightedRoundRobinBalancer;
import com.taofen8.mid.kong.config.Config.ConfigEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class CocoClient {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  final LoadBalancer balancer;
  final Codec codec;
  final OkHttpClient httpClient;
  final Map<String, String> globalHeaders;
  final boolean enableRequestGzip;
  private String specifiedBalancerNodesConfig;
  private String globalBalancerNodesConfig;


  public Map<String, String> globalHeaders() {
    return globalHeaders;
  }

  public LoadBalancer balancer() {
    return balancer;
  }

  public Codec codec() {
    return codec;
  }

  public OkHttpClient httpClient() {
    return httpClient;
  }

  public CocoCall newCall(CocoRequest request) {
    return new CocoRealCall(this, request);
  }

  public String getNextNode() throws LoadBalancerException {
    if (StringUtils.isEmpty(specifiedBalancerNodesConfig)) {
      String balancerNodesConfig =
          ConfigProvider.getConfig() != null ? ConfigProvider.getConfig()
              .getConfig(ConfigEntry.KONG_CALLER_BALANCER_NODES) : null;
      if (balancerNodesConfig != null) {
        reloadBalanceNodesConfig(balancerNodesConfig);
      } else {
        throw new LoadBalancerException("balancerNodesConfig can not be empty ");
      }
    }
    return balancer.nextNodeUrl();
  }

  private void reloadBalanceNodesConfig(String balancerNodesConfig) throws LoadBalancerException {
    if (!balancerNodesConfig
        .equals(this.globalBalancerNodesConfig)) {
      synchronized (this) {
        if (!balancerNodesConfig
            .equals(this.globalBalancerNodesConfig)) {
          if (logger.isInfoEnabled()) {
            logger.info(String.format("%s reload Config,old config:%s ,new Config:%s",
                this.getClass().getSimpleName(), this.globalBalancerNodesConfig,
                balancerNodesConfig));
          }
          balancer.loadConfig(balancerNodesConfig);
          this.globalBalancerNodesConfig = balancerNodesConfig;
        }
      }
    }
  }

  public String getConsumerKey() {
    return ConfigProvider.getInternalConfig().getConfig(InternalConfigEntry.COMSUMER_KEY);
  }

  public String getConsumerKeyName() {
    return ConfigProvider.getConfig().getConfig(ConfigEntry.KONG_CALLER_KEYAUTH_KEYNAME);
  }


  public CocoClient(CocoClient.Builder builder) {
    this.specifiedBalancerNodesConfig = builder.specifiedBalancerNodesConfig;

    String balancerNodesConfig = builder.specifiedBalancerNodesConfig;
    if (StringUtils.isEmpty(specifiedBalancerNodesConfig)) {
      balancerNodesConfig = ConfigProvider.getConfig()
          .getConfig(ConfigEntry.KONG_CALLER_BALANCER_NODES);
      globalBalancerNodesConfig = balancerNodesConfig;
    }

    if (builder.balancer == null) {
      builder.balancer = new WeightedRoundRobinBalancer();
    }
    this.balancer = builder.balancer;
    if (!StringUtils.isEmpty(balancerNodesConfig)) {
      try {
        balancer.loadConfig(balancerNodesConfig);
      } catch (LoadBalancerException e) {
        throw new IllegalArgumentException(e);
      }
    }

    this.codec = builder.codec;
    this.globalHeaders = builder.globalHeaders;
    this.enableRequestGzip = builder.enableRequestGzip;

    httpClient = new OkHttpClient.Builder().connectTimeout(builder.connectTimeout,
        TimeUnit.MILLISECONDS).readTimeout(builder.readTimeout,
        TimeUnit.MILLISECONDS).writeTimeout(builder.writeTimeout,
        TimeUnit.MILLISECONDS).build();


  }

  public static class Builder {

    private String specifiedBalancerNodesConfig;
    private LoadBalancer balancer;
    private Map<String, String> globalHeaders = new HashMap<>(4);
    private Codec codec;
    private boolean enableRequestGzip = false;

    int connectTimeout = 10000; //unit ：MILLISECONDS
    int readTimeout = 10000; //unit ：MILLISECONDS
    int writeTimeout = 10000; //unit ：MILLISECONDS


    public Builder globalHeader(String headerName, String headerValue) {
      this.globalHeaders.put(headerName, headerValue);
      return this;
    }

    public Builder balancer(LoadBalancer balancer) {
      this.balancer = balancer;
      return this;
    }

    public Builder specifiedBalancerNodesConfig(String specifiedBalancerNodesConfig) {
      this.specifiedBalancerNodesConfig = specifiedBalancerNodesConfig;
      return this;
    }

    public Builder enableRequestGzip(boolean enableRequestGzip) {
      this.enableRequestGzip = enableRequestGzip;
      return this;
    }

    public Builder codec(Codec codec) {
      this.codec = codec;
      return this;
    }

    public Builder connectTimeout(int milliseconds) {
      if (milliseconds > 0) {
        this.connectTimeout = milliseconds;
      }
      return this;
    }

    public Builder readTimeout(int milliseconds) {
      if (milliseconds > 0) {
        this.readTimeout = milliseconds;
      }
      return this;
    }

    public Builder writeTimeout(int milliseconds) {
      if (milliseconds > 0) {
        this.writeTimeout = milliseconds;
      }
      return this;
    }

    public CocoClient build() {
      return new CocoClient(this);
    }
  }


}
