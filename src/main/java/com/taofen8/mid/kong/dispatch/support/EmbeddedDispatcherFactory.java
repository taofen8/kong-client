/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.dispatch.support;

import com.taofen8.mid.kong.dispatch.embed.EmbeddedKongMappingDispatcher;
import com.taofen8.mid.kong.dispatch.embed.EmbeddedSpringMappingDispatcher;
import com.taofen8.mid.kong.dispatch.exception.KongConfigException;
import com.taofen8.mid.kong.dispatch.KongDispatcher;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmbeddedDispatcherFactory {

  private static Logger logger = LoggerFactory.getLogger(EmbeddedDispatcherFactory.class);

  private static String KONG_MAPPING = "kongmapping";
  private static String SPRING_MAPPING = "springmapping";

  private static Map<String, Class<?>> strategies = null;

  private EmbeddedDispatcherFactory() {
  }

  static {
    strategies = new HashMap<>(4);
    strategies.put(KONG_MAPPING, EmbeddedKongMappingDispatcher.class);
    strategies.put(SPRING_MAPPING, EmbeddedSpringMappingDispatcher.class);
  }

  public static KongDispatcher newEmbeddedDispatcher(String strategry) throws KongConfigException {
    Class clazz = strategies.get(strategry.toLowerCase());
    if (clazz != null) {
      try {
        return (KongDispatcher) clazz.newInstance();
      } catch (InstantiationException e) {
        logger.error("newEmbeddedDispatcher error", e);
      } catch (IllegalAccessException e) {
        logger.error("newEmbeddedDispatcher error", e);
      }
    }
    throw new KongConfigException(
        "specified proxy strategry[" + strategry + "] do not be supported");
  }

}
