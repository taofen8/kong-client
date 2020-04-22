/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin.api;

import com.taofen8.mid.kong.admin.request.ServiceReq;
import com.taofen8.mid.kong.admin.response.BaseResp;
import com.taofen8.mid.kong.admin.response.ServicePageResp;
import com.taofen8.mid.kong.admin.response.ServiceResp;
import java.io.IOException;

public interface IServiceAPI {
  
  ServiceResp addService(ServiceReq req) throws IOException;

  ServiceResp retrieveService(String nameOrId) throws IOException;

  ServiceResp retrieveServiceByRouteId(String id) throws IOException;

  ServicePageResp listServices(String offset, Integer size) throws IOException;

  ServiceResp updateService(String nameOrId, ServiceReq req) throws IOException;

  ServiceResp updateServiceByRouteId(String id, ServiceReq req) throws IOException;

  ServiceResp updateOrCreateService(String nameOrId, ServiceReq req) throws IOException;

  BaseResp deleteService(String nameOrId) throws IOException;
}
