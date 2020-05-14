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
   *
   * @return 服务请求路径
   */
  String path() default "";

  /**
   * @return  请求支持的host
   */
  String[] hosts() default {};

  /**
   *
   * @return 请求支持的方法协议
   */
  RequestMethod[] methods() default {RequestMethod.GET, RequestMethod.POST};


  /**
   *
   * @return 是否json格式的数据
   */
  boolean isJSONContextType() default  true;

}
