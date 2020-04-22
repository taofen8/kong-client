/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.register.struct;

public class Signature {

  final String methodName;
  final Class<?> requestType;
  final Class<?> responseType;

  public Signature(Builder builder) {
    this.methodName = builder.methodName;
    this.requestType = builder.requestType;
    this.responseType = builder.responseType;
  }

  public String methodName() {
    return methodName;
  }

  public Class<?> requestType() {
    return requestType;
  }

  public Class<?> responseType() {
    return responseType;
  }

  public static class Builder {

    private String methodName;
    private Class<?> requestType;
    private Class<?> responseType;

    public Builder methodName(String methodName) {
      this.methodName = methodName;
      return this;
    }

    public Builder requestType(Class<?> requestType) {
      this.requestType = requestType;
      return this;
    }

    public Builder responseType(Class<?> responseType) {
      this.responseType = responseType;
      return this;
    }

    public Signature build() {
      return new Signature(this);
    }
  }
}
