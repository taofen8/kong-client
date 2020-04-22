/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.config;

import com.taofen8.mid.kong.config.internal.AuthKeyGetter;
import com.taofen8.mid.kong.config.internal.AuthKeyGetter.AuthKeyGetterCallBack;
import com.taofen8.mid.kong.config.internal.InternalConfig;
import com.taofen8.mid.kong.executor.SingleTask;
import com.taofen8.mid.kong.config.Config.ConfigEntry;
import com.taofen8.mid.kong.executor.SingleExecutor;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

public interface ConfigLoader {

  Config buildConfig();

  @ConfigurationProperties(prefix = "kong.config")
  class DefaultConfigLoader implements ConfigLoader {

    protected static Logger logger = LoggerFactory.getLogger(DefaultConfigLoader.class);

    @Value("${kong.config.server.admin.url:}")
    private String kongServerAdminUrl;

    @Value("${kong.config.server.app.identifier:}")
    private String appIdentifier;

    @Value("${kong.config.server.route.hosts:}")
    private String kongServerHosts;

    @Value("${kong.config.server.proxy.strategy:}")
    private String proxyStrategy;

    @Value("${kong.config.server.healthcheck.config:}")
    private String healthCheckConfig;

    @Value("${kong.config.caller.balancer.nodes:}")
    private String balancerNodes;

    @Value("${kong.config.caller.keyauth.enable:}")
    private String keyauthEnable;

    @Value("${{kong.config.caller.keyauth.keyname:apikey}")
    private String keyauthKeyname;


    private Config config;

    @PostConstruct
    public void init() {
      final Config sConfig = ConfigProvider.getConfig();
      sConfig.configure(ConfigEntry.KONG_SERVER_ADMIN_URL, kongServerAdminUrl);
      sConfig.configure(ConfigEntry.KONG_SERVER_APP_IDENTIFIER, appIdentifier);
      sConfig.configure(ConfigEntry.KONG_SERVER_ROUTE_HOSTS, kongServerHosts);
      sConfig.configure(ConfigEntry.KONG_SERVER_PROXY_STRATEGY, proxyStrategy);
      sConfig.configure(ConfigEntry.KONG_SERVER_HEALTHCHECKS, healthCheckConfig);

      sConfig.configure(ConfigEntry.KONG_CALLER_BALANCER_NODES, balancerNodes);
      sConfig.configure(ConfigEntry.KONG_CALLER_KEYAUTH_ENABLE, keyauthEnable);
      sConfig.configure(ConfigEntry.KONG_CALLER_KEYAUTH_KEYNAME, keyauthKeyname);

      this.config = sConfig;


      InternalConfig internalConfig = ConfigProvider.getInternalConfig();
      if (ConfigResolver.isWitchOn(config.getConfig(ConfigEntry.KONG_CALLER_KEYAUTH_ENABLE))) {
        if (StringUtils.isEmpty(config.getConfig(ConfigEntry.KONG_SERVER_ADMIN_URL))) {
          logger.error(
              "kong config [kong.config.server.admin.url] could not be empty while [kong.config.caller.keyauth.enable] is switched on");
        } else if (StringUtils.isEmpty(config.getConfig(ConfigEntry.KONG_SERVER_APP_IDENTIFIER))) {
          logger.error(
              "kong config [tfg.kong.config.server.app.identifier] could not be empty while [kong.config.caller.keyauth.enable] is switched on");
        } else {
          SingleTask singleTask = new SingleTask<String>(
              new AuthKeyGetter(config.getConfig(ConfigEntry.KONG_SERVER_ADMIN_URL),
                  config.getConfig(ConfigEntry.KONG_SERVER_APP_IDENTIFIER)),
              new AuthKeyGetterCallBack(internalConfig));
          SingleExecutor.submit(singleTask);

        }
      }
    }


    @Override
    public Config buildConfig() {
      return this.config;
    }
  }

}
