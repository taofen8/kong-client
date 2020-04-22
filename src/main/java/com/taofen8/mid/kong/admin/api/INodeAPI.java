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

  /**
   * Node API Started at <a>https://docs.konghq.com/0.14.x/admin-api/#retrieve-node-information<a/>
   */
  RetrieveNodeInfoResp retrieveNodeInfo() throws IOException;

  RetrieveNodeStatusResp retrieveNodeStatus() throws IOException;
}
