/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin.api;

import com.taofen8.mid.kong.admin.response.RetrieveNodeInfoResp;
import com.taofen8.mid.kong.admin.response.RetrieveNodeStatusResp;
import java.io.IOException;

public interface INodeAPI {

  RetrieveNodeInfoResp retrieveNodeInfo() throws IOException;

  RetrieveNodeStatusResp retrieveNodeStatus() throws IOException;
}
