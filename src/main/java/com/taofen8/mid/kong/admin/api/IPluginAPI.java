/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin.api;

import com.taofen8.mid.kong.admin.response.BaseResp;
import com.taofen8.mid.kong.admin.response.EnabledPluginsResp;
import com.taofen8.mid.kong.admin.response.PluginPageResp;
import com.taofen8.mid.kong.admin.response.PluginResp;
import com.taofen8.mid.kong.admin.response.PluginSchemaResp;
import com.taofen8.mid.kong.admin.query.PluginQuery;
import com.taofen8.mid.kong.admin.request.PluginReq;
import java.io.IOException;

public interface IPluginAPI {

  PluginResp addPlugin(PluginReq req) throws IOException;

  PluginResp retrievePlugin(String id) throws IOException;

  PluginPageResp listPlugins(PluginQuery query) throws IOException;

  PluginResp updatePlugin(String id, PluginReq req) throws IOException;

  PluginResp updateOrAddPlugin(PluginReq req) throws IOException;

  BaseResp deletePlugin(String id) throws IOException;

  EnabledPluginsResp retrieveEnabledPlugins() throws IOException;

  PluginSchemaResp retrievePluginSchema(String name) throws IOException;
}
