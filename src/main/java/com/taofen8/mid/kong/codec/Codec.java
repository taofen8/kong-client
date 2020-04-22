/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.codec;


import com.taofen8.mid.kong.caller.exception.CallerDecodeException;

public interface Codec {


  byte[] encode(byte[] data);

  byte[] decode(byte[] data) throws CallerDecodeException;

}
