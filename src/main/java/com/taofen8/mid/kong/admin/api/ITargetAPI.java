/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin.api;

import com.taofen8.mid.kong.admin.response.BaseResp;
import com.taofen8.mid.kong.admin.response.TargetPageResp;
import com.taofen8.mid.kong.admin.response.TargetResp;
import com.taofen8.mid.kong.admin.query.TargetQuery;
import com.taofen8.mid.kong.admin.request.TargetReq;
import java.io.IOException;

public interface ITargetAPI {

  /**
   * SNI API Start at <a>https://docs.konghq.com/0.14.x/admin-api/#target-object</a>
   */

  TargetResp addTarget(String id, TargetReq req) throws IOException;

  TargetPageResp listTargets(String nameOrId, TargetQuery query) throws IOException;

  TargetPageResp listAllTargets(String nameOrId) throws IOException;

  BaseResp deleteTarget(String upsteamNameOrId, String targetOrId) throws IOException;

  BaseResp setTargetAsHealthy(String upsteamNameOrId, String targetOrId) throws IOException;

  BaseResp setTargetAsUnhealthy(String upsteamNameOrId, String targetOrId) throws IOException;

  boolean containsTarget(String upstreamNameOrId, String target) throws IOException;
}
