/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin.api;

import com.taofen8.mid.kong.admin.request.UpstreamReq;
import com.taofen8.mid.kong.admin.response.BaseResp;
import com.taofen8.mid.kong.admin.response.EndpointPageResp;
import com.taofen8.mid.kong.admin.response.UpstreamPageResp;
import com.taofen8.mid.kong.admin.response.UpstreamResp;
import java.io.IOException;

public interface IUpstreamAPI {


  UpstreamResp addUpstream(UpstreamReq req) throws IOException;

  UpstreamResp retrieveUpstream(String nameOrId) throws IOException;

  UpstreamPageResp listUpstreams() throws IOException;

  UpstreamResp updateUpstream(String nameOrId, UpstreamReq req) throws IOException;

  UpstreamResp updateOrCreateUpstream(String nameOrId, UpstreamReq req) throws IOException;

  UpstreamResp retrieveUpstreamByTarget(String hostPortOrId) throws IOException;

  BaseResp deleteUpstream(String nameOrId) throws IOException;

  EndpointPageResp endpoint(String nameOrId) throws IOException;
}
