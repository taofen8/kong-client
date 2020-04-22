/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin.api;

import com.taofen8.mid.kong.admin.request.KeyAuthReq;
import com.taofen8.mid.kong.admin.response.KeyAuthPageResp;
import com.taofen8.mid.kong.admin.response.KeyAuthResp;
import java.io.IOException;

public interface IKeyAuthAPI {


  KeyAuthResp createKeyAuth(String consumerNameOrId, KeyAuthReq req) throws IOException;

  KeyAuthPageResp listKeyAuths(String consumerNameOrId) throws IOException;
}
