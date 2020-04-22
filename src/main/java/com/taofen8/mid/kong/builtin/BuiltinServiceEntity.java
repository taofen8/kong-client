/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.builtin;

import com.taofen8.mid.kong.register.struct.Invocation;
import com.taofen8.mid.kong.register.struct.Service;

public class BuiltinServiceEntity extends Service {

  final BuiltinServiceMapping serviceMapping;
  final Invocation invocation;


  public Invocation invocation() {
    return this.invocation;
  }

  public BuiltinServiceMapping serviceMapping() {
    return this.serviceMapping;
  }

  public BuiltinServiceEntity(Builder builder) {
    super(builder);
    this.serviceMapping = builder.serviceMapping;
    this.invocation = builder.invocation;
  }


  public static class Builder extends Service.Builder<Builder> {

    private BuiltinServiceMapping serviceMapping;
    private Invocation invocation;

    public Builder serviceMapping(BuiltinServiceMapping serviceMapping) {
      this.serviceMapping = serviceMapping;
      return this;
    }


    public Builder invocation(Invocation invocation) {
      this.invocation = invocation;
      return this;
    }

    @Override
    public BuiltinServiceEntity build() {
      return new BuiltinServiceEntity(this);
    }

  }

}
