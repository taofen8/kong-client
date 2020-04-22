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
  /**
   * SNI API
   * Start at <a>https://docs.konghq.com/0.14.x/admin-api/#add-sni</a>
   */

  /**
   * @// TODO: 2018/12/6   haven't tested yet
   */
  SNIResp addSNI(SNIReq req) throws IOException;

  SNIResp retrieveSNI(String nameOrId) throws IOException;

  SNIPageResp listSNIs() throws IOException;

  SNIResp updateSNI(String nameOrId, SNIReq req) throws IOException;

  SNIResp updateOrCreateSNI(String nameOrId, SNIReq req) throws IOException;

  BaseResp deleteSNI(String nameOrId) throws IOException;
}
