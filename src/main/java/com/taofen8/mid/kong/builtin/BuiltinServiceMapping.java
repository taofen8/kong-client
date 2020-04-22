/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.builtin;

import com.taofen8.mid.kong.register.struct.RequestMethod;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BuiltinServiceMapping {

  /**
   * 服务请求路径
   *
   * @return
   */
  String path() default "";

  /**
   * 请求支持的host
   * @return
   */
  String[] hosts() default {};

  /**
   * 请求支持的方法协议
   *
   * @return
   */
  RequestMethod[] methods() default {RequestMethod.GET, RequestMethod.POST};


}
