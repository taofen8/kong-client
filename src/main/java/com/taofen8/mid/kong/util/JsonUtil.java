/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class JsonUtil {

  private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);
  private static ObjectMapper mapper = new ObjectMapper();

  static {
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    mapper.setSerializationInclusion(Include.NON_NULL);
  }

  private JsonUtil() {
  }

  public static String toJSONString(Object object) {
    try {
      return mapper.writeValueAsString(object);
    } catch (Exception e) {
      logger.error("invoke toJSONString error ,", e);
    }
    return null;
  }

  public static <T> T parseObject(String jsonString, Class<T> clazz) {
    try {
      if (StringUtils.isEmpty(jsonString)) {
        return null;
      }
      return (T) mapper.readValue(jsonString, clazz);
    } catch (IOException e) {
      logger.error("invoke parseObject error ,", e);
    }
    return null;
  }
}
