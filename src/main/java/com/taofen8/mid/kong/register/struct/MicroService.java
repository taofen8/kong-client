/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.register.struct;

import com.taofen8.mid.kong.annotation.KongServiceMapping;

public class MicroService extends Service {

  final KongServiceMapping serviceMapping;
  final Invocation invocation;


  public Invocation invocation(){
    return this.invocation;
  }

  public KongServiceMapping serviceMapping(){
    return this.serviceMapping;
  }

  public MicroService(Builder builder) {
    super(builder);
    this.serviceMapping = builder.serviceMapping;
    this.invocation = builder.invocation;
  }


  public static class Builder extends Service.Builder<Builder> {

    private KongServiceMapping serviceMapping;
    private Invocation invocation;

    public Builder serviceMapping(KongServiceMapping serviceMapping) {
      this.serviceMapping = serviceMapping;
      return this;
    }


    public Builder invocation(Invocation invocation) {
      this.invocation = invocation;
      return this;
    }

    @Override
    public MicroService build() {
      return new MicroService(this);
    }

  }

}
