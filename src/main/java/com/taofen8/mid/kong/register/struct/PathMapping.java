/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.register.struct;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

public class PathMapping {

  Logger logger = LoggerFactory.getLogger(this.getClass());
  private String path;
  private String kongPath;
  private RequestMappingInfo requestMappingInfo;
  private String upstreamName;
  private String localName;

  public String getPath() {
    return path;
  }

  public String getKongPath() {
    return kongPath;
  }

  public RequestMappingInfo getRequestMappingInfo() {
    return requestMappingInfo;
  }

  public String getUpstreamName() {
    return upstreamName;
  }

  public String getLocalName() {
    return localName;
  }

  public PathMapping(Builder builder) {
    builder.build(this);
  }

  public static class Builder {

    private String path;
    private String upstreamName;
    private RequestMappingInfo requestMappingInfo;


    public Builder path(String path) {
      this.path = path;
      return this;
    }

    public Builder upstreamName(String upstreamName) {
      this.upstreamName = upstreamName;
      return this;
    }

    public Builder requestMappingInfo(RequestMappingInfo requestMappingInfo) {
      this.requestMappingInfo = requestMappingInfo;
      return this;
    }

    public PathMapping build(PathMapping mapping) {
      mapping.path = this.path;
      mapping.requestMappingInfo = this.requestMappingInfo;
      mapping.upstreamName = this.upstreamName;

      KongPathResolver resolver = new KongPathResolver(path);
      mapping.kongPath = resolver.kongPath;
      mapping.localName = resolver.localName;
      return mapping;
    }

    class KongPathResolver {

      private final Pattern GLOB_PATTERN = Pattern
          .compile("\\?|\\*|\\{((?:\\{[^/]+?\\}|[^/{}]|\\\\[{}])+?)\\}|/");
      private final String DEFAULT_VARIABLE_PATTERN = "(.*)";

      String path;
      String kongPath;
      String localName;

      KongPathResolver(String path) {
        this.path = path;

        StringBuilder kongPathBuilder = new StringBuilder();
        StringBuilder serviceNameBuilder = new StringBuilder();
        Matcher matcher = GLOB_PATTERN.matcher(path);
        int end = 0;
        while (matcher.find()) {
          kongPathBuilder.append(path, end, matcher.start());
          serviceNameBuilder.append(path, end, matcher.start());
          String match = matcher.group();
          if ("?".equals(match)) {
            kongPathBuilder.append('.');
            serviceNameBuilder.append('_');
          } else if ("*".equals(match)) {
            kongPathBuilder.append(".*");
            serviceNameBuilder.append('_');
          } else if (match.startsWith("{") && match.endsWith("}")) {
            kongPathBuilder.append(DEFAULT_VARIABLE_PATTERN);
            serviceNameBuilder.append(match.substring(1, match.length() - 1));
          } else if ("/".equals(match)) {
            kongPathBuilder.append("/");
            serviceNameBuilder.append(".");
          }
          end = matcher.end();
        }

        kongPathBuilder.append(path, end, path.length());
        serviceNameBuilder.append(path, end, path.length());

        this.kongPath = kongPathBuilder.toString();
        this.localName = serviceNameBuilder.toString();
        if (this.localName.startsWith(".")) {
          this.localName = this.localName.substring(1, localName.length());
        }
      }

    }
  }


}
