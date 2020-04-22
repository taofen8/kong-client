/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.dispatch;

import com.taofen8.mid.kong.annotation.KongServiceMapping;

public class RequestContext<R,S> {

  private Class<R> requestClass;
  private Class<S> responseClass;
  private KongServiceMapping serviceMapping;

  public Class<R> getRequestClass() {
    return requestClass;
  }

  public Class<S> getResponseClass() {
    return responseClass;
  }

  public KongServiceMapping getServiceMapping() {
    return serviceMapping;
  }

  public static class Builder<R,S> {

    private Class<R> requestClass;
    private Class<S> responseClass;
    private KongServiceMapping serviceMapping;

    public Builder requestClass(Class<R> requestClass) {
      this.requestClass = requestClass;
      return this;
    }

    public Builder responseClass(Class<S> responseClass) {
      this.responseClass = responseClass;
      return this;
    }

    public Builder serviceMapping(KongServiceMapping serviceMapping) {
      this.serviceMapping = serviceMapping;
      return this;
    }

    public RequestContext build() {
      RequestContext context = new RequestContext();
      context.requestClass = this.requestClass;
      context.responseClass = this.responseClass;
      context.serviceMapping = this.serviceMapping;
      return context;
    }
  }
}
