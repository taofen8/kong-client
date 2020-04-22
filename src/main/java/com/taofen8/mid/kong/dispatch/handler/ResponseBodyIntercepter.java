/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */
package com.taofen8.mid.kong.dispatch.handler;

/**
 * Intecepting response body aims to handle codec ， compress etc. before writing http response .
 * Note that{@link KongResponse#responseBody} has been not set in this phase
 */
public interface ResponseBodyIntercepter {

  byte[] intercept(byte[] bytesOfBody, KongRequest request, KongResponse response);
}
