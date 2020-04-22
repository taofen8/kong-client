/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin.api;

import com.taofen8.mid.kong.admin.request.CertificateReq;
import com.taofen8.mid.kong.admin.response.BaseResp;
import com.taofen8.mid.kong.admin.response.CertificatePageResp;
import com.taofen8.mid.kong.admin.response.CertificateResp;
import java.io.IOException;

public interface ICertificateAPI {
  /**
   * Certificate API
   * Start at <a>https://docs.konghq.com/0.14.x/admin-api/#add-certificate</a>
   */

  /**
   * @// TODO: 2018/12/6   haven't tested yet
   */
  CertificateResp addCertificate(CertificateReq req) throws IOException;

  CertificatePageResp listCertificates() throws IOException;

  /**
   * @// TODO: 2018/12/6   haven't tested yet
   */
  CertificateResp retrieveCertificate(String sniOrId) throws IOException;

  /**
   * @// TODO: 2018/12/6   haven't tested yet
   */
  CertificateResp updateCertificate(String sniOrId, CertificateReq req) throws IOException;

  /**
   * @// TODO: 2018/12/6   haven't tested yet
   */
  CertificateResp updateOrCreateCertificate(String sniOrId, CertificateReq req) throws IOException;

  /**
   * @// TODO: 2018/12/6   haven't tested yet
   */
  BaseResp deleteCertificate(String sniOrId) throws IOException;
}
