/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin.response;


import com.taofen8.mid.kong.util.JsonUtil;
import java.util.List;

public class EnabledPluginsResp extends BaseResp {

  private List<String> enabled_plugins;

  public List<String> getEnabled_plugins() {
    return enabled_plugins;
  }

  public void setEnabled_plugins(List<String> enabled_plugins) {
    this.enabled_plugins = enabled_plugins;
  }

  @Override
  public String toString() {
    String header = super.toString();

    StringBuilder builder = new StringBuilder();
    builder.append(header);
    builder.append("\n");
    builder.append(JsonUtil.toJSONString(this));
    return builder.toString();
  }
}
