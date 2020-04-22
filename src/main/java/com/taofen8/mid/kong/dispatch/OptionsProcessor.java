/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.dispatch;

import com.taofen8.mid.kong.register.struct.Service;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;

public abstract class OptionsProcessor {

  Pattern optionsOriginPattern = Pattern.compile("(http[s]?://)?(.*)");

  /**
   * 支持Options的跨域域名，多个以逗号分隔
   *
   * @return
   */
  public abstract String getAllowOrigins();

  public boolean processOptions(HttpServletRequest request, HttpServletResponse response,
      Service service) {
    if ("OPTIONS".equals(request.getMethod())) {
      String allowOrigins = this.getAllowOrigins();
      String origin = request.getHeader("Origin");
      if (!StringUtils.isEmpty(allowOrigins) && !StringUtils.isEmpty(origin)) {
        Matcher matcher = optionsOriginPattern.matcher(origin);
        if (matcher.find() && allowOrigins.contains(matcher.group(2))) {
          response.addHeader("Access-Control-Allow-Origin", origin);
          response.addHeader("Access-Control-Allow-Methods",
              Arrays.asList(service.route().methods()).toString());
          response.addHeader("Access-Control-Max-Age", "1800");
          response.setStatus(200);
          return true;
        }
      }
      response.setStatus(405);
      return true;
    } else {
      return false;
    }
  }
}
