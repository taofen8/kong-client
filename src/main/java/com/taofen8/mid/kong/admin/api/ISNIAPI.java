/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin.api;


import com.taofen8.mid.kong.admin.response.BaseResp;
import com.taofen8.mid.kong.admin.response.SNIPageResp;
import com.taofen8.mid.kong.admin.response.SNIResp;
import com.taofen8.mid.kong.admin.request.SNIReq;
import java.io.IOException;

public interface ISNIAPI {

  SNIResp addSNI(SNIReq req) throws IOException;

  SNIResp retrieveSNI(String nameOrId) throws IOException;

  SNIPageResp listSNIs() throws IOException;

  SNIResp updateSNI(String nameOrId, SNIReq req) throws IOException;

  SNIResp updateOrCreateSNI(String nameOrId, SNIReq req) throws IOException;

  BaseResp deleteSNI(String nameOrId) throws IOException;
}
