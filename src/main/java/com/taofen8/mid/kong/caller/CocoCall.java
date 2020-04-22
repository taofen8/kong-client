/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.caller;

import com.taofen8.mid.kong.caller.exception.CallerException;

public interface CocoCall {

  /**
   * 同步调用
   * @return
   * @throws CallerException
   */
  CocoResponse execute() throws CallerException;

  /**
   * 异步调用
   * @param callback
   * @throws CallerException
   */
  void enqueue(CocoCallback callback) throws CallerException;
}
