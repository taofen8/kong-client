/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.register.struct;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Invocation {

  final Object bean;
  final Signature signature;
  private Method method;

  public Invocation(Builder builder) {
    this.bean = builder.bean;
    this.signature = builder.signature;
  }

  public Object bean() {
    return bean;
  }

  public Signature signature() {
    return signature;
  }

  private Method getMethod() {
    if (null == this.method) {
      try {
        this.method = bean.getClass().getMethod(signature.methodName, signature.requestType);
      } catch (NoSuchMethodException e) {
        this.method = null;
      }
    }
    return this.method;
  }

  public Object invoke(Object request) throws InvocationTargetException, IllegalAccessException {
    Method m = this.getMethod();
    if (m == null) {
      return null;
    }
    return m.invoke(this.bean, request);
  }

  public static class Builder {

    private Object bean;
    private Signature signature;

    public Builder bean(Object bean) {
      this.bean = bean;
      return this;
    }

    public Builder signature(Signature signature) {
      this.signature = signature;
      return this;
    }

    public Invocation build() {
      return new Invocation(this);
    }
  }
}
