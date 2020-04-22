/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.dispatch.handler;


public interface ResponseHandler<R, S> {

  void handle(KongRequest<R> request, KongResponse<S> response) throws Exception;
}
