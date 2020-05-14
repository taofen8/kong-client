/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.spring;

import com.taofen8.mid.kong.config.ConfigLoader.DefaultConfigLoader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KongAutoConfigure {

  @Bean(name = "defaultKongConfigLoader")
  @ConditionalOnMissingBean(type = "com.taofen8.mid.kong.config.ConfigLoader")
  public DefaultConfigLoader getKongConfigLoader() {
    return new DefaultConfigLoader();
  }

}
