/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin.api;

import com.taofen8.mid.kong.admin.response.BaseResp;
import com.taofen8.mid.kong.admin.response.RoutePageResp;
import com.taofen8.mid.kong.admin.response.RouteResp;
import com.taofen8.mid.kong.admin.request.RouteReq;
import java.io.IOException;

public interface IRouteAPI {

  /**
   * Route API Started at <a>https://docs.konghq.com/0.14.x/admin-api/#add-route</a>
   */
  RouteResp addRoute(RouteReq req) throws IOException;

  RouteResp retrieveRoute(String id) throws IOException;

  RoutePageResp listRoutes(String offset, Integer size) throws IOException;

  RoutePageResp listRoutesByService(String nameOrId) throws IOException;

  RoutePageResp listRoutesNext(String next) throws IOException;

  RouteResp updateRoute(String id, RouteReq req) throws IOException;

  RouteResp updateOrCreateRoute(String routeNameOrId, RouteReq req)
      throws IOException;

  BaseResp deleteRoute(String id) throws IOException;

  BaseResp deleteRoutesByService(String nameOrId) throws IOException;

  BaseResp deleteAllRoutes() throws IOException;
}
