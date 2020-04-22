/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

public abstract class GzipUtil {

  private static Log log = LogFactory.getLog(GzipUtil.class);
  private static String encode = "utf-8";

  private GzipUtil() {
  }

  public static byte[] zip(byte[] btyes) {
    if (btyes != null && btyes.length > 0) {
      try (ByteArrayOutputStream out = new ByteArrayOutputStream();
          GZIPOutputStream gzip = new GZIPOutputStream(out);
      ) {

        gzip.write(btyes);
        return out.toByteArray();
      } catch (IOException var4) {
        log.error("zip error ", var4);
      }
    }
    return new byte[0];
  }


  public static byte[] zip(String str, String encoding) {
    if (str != null && str.length() > 0) {
      byte[] bytes;
      try {
        if (StringUtils.isEmpty(encoding)) {
          encoding = encode;
        }
        bytes = str.getBytes(encoding);
        return zip(bytes);
      } catch (UnsupportedEncodingException e) {
        log.error("zip error ", e);
      }
    }
    return new byte[0];
  }

  public static byte[] unzip(byte[] b) {
    if (b != null && b.length > 0) {

      try (
          ByteArrayOutputStream out = new ByteArrayOutputStream();
          ByteArrayInputStream in = new ByteArrayInputStream(b);
          GZIPInputStream gunzip = new GZIPInputStream(in);
      ) {
        byte[] buffer = new byte[256];

        int n;
        while ((n = gunzip.read(buffer)) >= 0) {
          out.write(buffer, 0, n);
        }

        return out.toByteArray();
      } catch (IOException var20) {
        log.error("unzip error ", var20);
      }
    }
    return new byte[0];
  }

  public static byte[] unzip(InputStream in) {
    if (in != null) {

      try (
          ByteArrayOutputStream out = new ByteArrayOutputStream();
          GZIPInputStream gunzip = new GZIPInputStream(in);
      ) {

        byte[] buffer = new byte[256];

        int n;
        while ((n = gunzip.read(buffer)) >= 0) {
          out.write(buffer, 0, n);
        }

        return out.toByteArray();
      } catch (IOException var20) {
        log.error("unzip error ", var20);
      }
    }
    return new byte[0];
  }
}
