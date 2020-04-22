/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

public class BeanFactory {

  private static Logger logger = LoggerFactory.getLogger(BeanFactory.class);

  private BeanFactory() {
  }

  public static <T> T getBeanForTypeIncludingAncestors(ApplicationContext context, Class<T> clazz) {
    T bean = null;
    try {
      bean = context.getBean(clazz);
    } catch (BeansException e) {
      if (context.getParent() != null) {
        try {
          bean = context.getParent().getBean(clazz);
        } catch (BeansException ex) {
          logger.error("getBeanForTypeIncludingAncestors error,", e);
        }
      }
    }
    return bean;
  }
}
