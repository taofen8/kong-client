/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.executor;

import java.util.concurrent.Callable;

public class SingleTask<V> {

  private Callable<V> callable;
  private ICallBack<V> callBack;

  public SingleTask(Callable<V> callable, ICallBack<V> callBack) {
    this.callable = callable;
    this.callBack = callBack;
  }

  public Callable<V> getCallable() {
    return callable;
  }

  public ICallBack<V> getCallBack() {
    return callBack;
  }

  public interface ICallBack<V> {

    void callBack(V v);
  }

}
