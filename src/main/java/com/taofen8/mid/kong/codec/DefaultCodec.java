/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.codec;

import com.taofen8.mid.kong.caller.exception.CallerDecodeException;
import java.nio.ByteBuffer;

public class DefaultCodec implements Codec {

  private final int DEFAULT_FLAG = 0x79;
  private int flag = DEFAULT_FLAG;

  public DefaultCodec() {
  }

  public DefaultCodec(int flag) {
    this.flag = flag;
  }

  public byte[] encode(byte[] data) {
    ByteBuffer buf = ByteBuffer.allocate(4 + data.length);
    buf.put(intToByteArray(flag));
    buf.put(data);
    return buf.array();
  }

  public byte[] decode(byte[] data) throws CallerDecodeException {
    ByteBuffer buf = ByteBuffer.wrap(data);
    int innerFlag = buf.getInt();
    if (flag != innerFlag) {
      throw new CallerDecodeException();
    }

    byte[] dst = new byte[data.length - 4];
    buf.get(dst, 0, data.length - 4);
    return dst;
  }

  byte[] intToByteArray(int a) {
    return new byte[]{
        (byte) ((a >> 24) & 0xFF),
        (byte) ((a >> 16) & 0xFF),
        (byte) ((a >> 8) & 0xFF),
        (byte) (a & 0xFF)
    };
  }
}
