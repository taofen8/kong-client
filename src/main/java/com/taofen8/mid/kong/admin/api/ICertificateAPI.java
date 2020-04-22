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

  CertificateResp addCertificate(CertificateReq req) throws IOException;

  CertificatePageResp listCertificates() throws IOException;


  CertificateResp retrieveCertificate(String sniOrId) throws IOException;


  CertificateResp updateCertificate(String sniOrId, CertificateReq req) throws IOException;


  CertificateResp updateOrCreateCertificate(String sniOrId, CertificateReq req) throws IOException;


  BaseResp deleteCertificate(String sniOrId) throws IOException;
}
