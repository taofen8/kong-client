/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin;


import com.taofen8.mid.kong.admin.api.ICertificateAPI;
import com.taofen8.mid.kong.admin.api.IConsumerAPI;
import com.taofen8.mid.kong.admin.api.IKeyAuthAPI;
import com.taofen8.mid.kong.admin.api.INodeAPI;
import com.taofen8.mid.kong.admin.api.IPluginAPI;
import com.taofen8.mid.kong.admin.api.IRouteAPI;
import com.taofen8.mid.kong.admin.api.ISNIAPI;
import com.taofen8.mid.kong.admin.api.IServiceAPI;
import com.taofen8.mid.kong.admin.api.ITargetAPI;
import com.taofen8.mid.kong.admin.api.IUpstreamAPI;
import com.taofen8.mid.kong.admin.query.PluginQuery;
import com.taofen8.mid.kong.admin.query.TargetQuery;
import com.taofen8.mid.kong.admin.request.BaseReq;
import com.taofen8.mid.kong.admin.request.CertificateReq;
import com.taofen8.mid.kong.admin.request.ConsumerReq;
import com.taofen8.mid.kong.admin.request.KeyAuthReq;
import com.taofen8.mid.kong.admin.request.PluginReq;
import com.taofen8.mid.kong.admin.request.RouteReq;
import com.taofen8.mid.kong.admin.request.SNIReq;
import com.taofen8.mid.kong.admin.request.ServiceReq;
import com.taofen8.mid.kong.admin.request.TargetReq;
import com.taofen8.mid.kong.admin.request.UpstreamReq;
import com.taofen8.mid.kong.admin.response.BaseResp;
import com.taofen8.mid.kong.admin.response.CertificatePageResp;
import com.taofen8.mid.kong.admin.response.CertificateResp;
import com.taofen8.mid.kong.admin.response.ConsumerPageResp;
import com.taofen8.mid.kong.admin.response.ConsumerResp;
import com.taofen8.mid.kong.admin.response.EnabledPluginsResp;
import com.taofen8.mid.kong.admin.response.EndpointPageResp;
import com.taofen8.mid.kong.admin.response.KeyAuthPageResp;
import com.taofen8.mid.kong.admin.response.KeyAuthResp;
import com.taofen8.mid.kong.admin.response.PluginPageResp;
import com.taofen8.mid.kong.admin.response.PluginResp;
import com.taofen8.mid.kong.admin.response.PluginSchemaResp;
import com.taofen8.mid.kong.admin.response.RetrieveNodeInfoResp;
import com.taofen8.mid.kong.admin.response.RetrieveNodeStatusResp;
import com.taofen8.mid.kong.admin.response.RoutePageResp;
import com.taofen8.mid.kong.admin.response.RouteResp;
import com.taofen8.mid.kong.admin.response.SNIPageResp;
import com.taofen8.mid.kong.admin.response.SNIResp;
import com.taofen8.mid.kong.admin.response.ServicePageResp;
import com.taofen8.mid.kong.admin.response.ServiceResp;
import com.taofen8.mid.kong.admin.response.TargetPageResp;
import com.taofen8.mid.kong.admin.response.TargetResp;
import com.taofen8.mid.kong.admin.response.UpstreamPageResp;
import com.taofen8.mid.kong.admin.response.UpstreamResp;
import com.taofen8.mid.kong.util.JsonUtil;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JKongAdmin implements INodeAPI,
    IServiceAPI,
    IRouteAPI,
    IConsumerAPI,
    IPluginAPI,
    ICertificateAPI,
    ISNIAPI,
    IUpstreamAPI,
    ITargetAPI,
    IKeyAuthAPI {

  Logger logger = LoggerFactory.getLogger(JKongAdmin.class);

  private String adminUrl;
  private static OkHttpClient client = new OkHttpClient();
  public static final MediaType JSONMedia
      = MediaType.parse("application/json; charset=utf-8");

  private final String SERVICE_PATH = "/services/";
  private final String ROUTE_PATH = "/routes/";
  private final String CONSUMER_PATH = "/consumers/";
  private final String PLUGIN_PATH = "/plugins";
  private final String CERTIFICATE_PATH = "/certificates/";
  private final String SNI_PATH = "/snis/";
  private final String UPSTREAM_PATH = "/upstreams/";
  private final String TARGET_PATH = "/targets/";

  private int timeoutRetry = 1;

  public JKongAdmin(String adminUrl) {
    this.adminUrl = adminUrl;
  }

  public void setTimeoutRetry(int timeoutRetry) {
    this.timeoutRetry = timeoutRetry;
  }

  private static class Method {

    public final static String GET = "get";
    public final static String POST = "post";
    public final static String PUT = "put";
    public final static String PATCH = "patch";
    public final static String DELETE = "delete";
  }

  public String buildUrl(String attachPath) {
    String url = this.adminUrl + attachPath;
    logger.info(url);
    return url;
  }

  private <T> T template(String method, String attachPath, Class<T> respOfT) throws IOException {
    return template(method, attachPath, null, respOfT);
  }

  /**
   * Inner invoke function, missing some exception check
   */
  private <T> T template(String method, String attachPath, BaseReq req, Class<T> respOfT)
      throws IOException {
    Request request = null;
    if (method.equals(Method.GET)) {
      request = new Request.Builder()
          .url(buildUrl(attachPath))
          .get()
          .build();
    } else if (method.equals(Method.POST)) {

      request = new Request.Builder()
          .url(buildUrl(attachPath))
          .post(RequestBody.create(JSONMedia, JsonUtil.toJSONString(req)))
          .build();
    } else if (method.equals(Method.DELETE)) {
      request = new Request.Builder()
          .url(buildUrl(attachPath))
          .delete()
          .build();
    } else if (method.equals(Method.PATCH)) {
      request = new Request.Builder()
          .url(buildUrl(attachPath))
          .patch(RequestBody.create(JSONMedia, JsonUtil.toJSONString(req)))
          .build();
    } else if (method.equals(Method.PUT)) {
      request = new Request.Builder()
          .url(buildUrl(attachPath))
          .put(RequestBody.create(JSONMedia, JsonUtil.toJSONString(req)))
          .build();
    } else {
      throw new IllegalArgumentException("http method [" + method + "] is not supported");
    }

    return fireRequest(request, respOfT);
  }

  private <T> T fireRequest(Request request, Class<T> respOfT) throws IOException {
    Response response = null;
    int retryTimes = 0;

    while (retryTimes < timeoutRetry) {
      try {
        response = client.newCall(request).execute();
        break;
      } catch (SocketTimeoutException timeoutException) {
        retryTimes++;
        if (retryTimes == timeoutRetry) {
          throw timeoutException;
        }
      } catch (Exception e) {
        throw e;
      }
    }

    if (response == null) {
      throw new IOException("response is null");
    }

    String responseStr = "";
    if (response.body() != null) {
      responseStr = response.body().string();
    }

    T resp = null;
    if (responseStr.length() == 0) {
      resp = (T) new BaseResp();
    } else {
      resp = JsonUtil.parseObject(responseStr, respOfT);
    }

    ((BaseResp) resp).setStatus(response.code());
    ((BaseResp) resp).setSchema(request.isHttps() ? BaseResp.Schema.HTTPS : BaseResp.Schema.HTTP);
    return resp;
  }

  /***************************************  Node ***********************************************************/


  public RetrieveNodeInfoResp retrieveNodeInfo() throws IOException {
    return template(Method.GET, "/", RetrieveNodeInfoResp.class);
  }

  public RetrieveNodeStatusResp retrieveNodeStatus() throws IOException {
    return template(Method.GET, "/status", RetrieveNodeStatusResp.class);
  }


  /***************************************  Service ***********************************************************/

  public ServiceResp addService(ServiceReq req) throws IOException {
    return template(Method.POST, SERVICE_PATH, req, ServiceResp.class);
  }

  public ServiceResp retrieveService(String nameOrId) throws IOException {
    return template(Method.GET, SERVICE_PATH + (nameOrId == null ? "" : nameOrId),
        ServiceResp.class);
  }

  public ServiceResp retrieveServiceByRouteId(String id) throws IOException {
    return template(Method.GET, ROUTE_PATH + (id == null ? "" : id) + "/service",
        ServiceResp.class);
  }

  public ServicePageResp listServices(String offset, Integer size) throws IOException {
    Integer localSize = Math.min(1000, (size == null ? 100 : size));
    String localAttachPath = offset == null ? ("/services?size=" + localSize)
        : ("/services?offset=" + offset + "&size=" + localSize);
    return template(Method.GET, localAttachPath, ServicePageResp.class);
  }

  public ServiceResp updateService(String nameOrId, ServiceReq req) throws IOException {
    return template(Method.PATCH, SERVICE_PATH + (nameOrId == null ? "" : nameOrId), req,
        ServiceResp.class);
  }

  public ServiceResp updateServiceByRouteId(String id, ServiceReq req) throws IOException {
    return template(Method.PATCH, SERVICE_PATH + (id == null ? "" : id) + "/service", req,
        ServiceResp.class);
  }

  public ServiceResp updateOrCreateService(String nameOrId, ServiceReq req) throws IOException {
    return template(Method.PUT, SERVICE_PATH + (nameOrId == null ? "" : nameOrId), req,
        ServiceResp.class);
  }

  public BaseResp deleteService(String nameOrId) throws IOException {
    return template(Method.DELETE, SERVICE_PATH + (nameOrId == null ? "" : nameOrId),
        BaseResp.class);
  }


  /***************************************  Route ***********************************************************/

  public RouteResp addRoute(RouteReq req) throws IOException {
    return template(Method.POST, ROUTE_PATH, req, RouteResp.class);
  }

  public RouteResp retrieveRoute(String id) throws IOException {
    return template(Method.GET, ROUTE_PATH + (id == null ? "" : id), RouteResp.class);
  }

  public RoutePageResp listRoutes(String offset, Integer size) throws IOException {
    Integer localSize = Math.min(1000, (size == null ? 100 : size));
    String localAttachPath = offset == null ? ("/routes?size=" + localSize)
        : ("/routes?offset=" + offset + "&size=" + localSize);
    return template(Method.GET, localAttachPath, RoutePageResp.class);
  }

  public RoutePageResp listRoutesByService(String nameOrId) throws IOException {
    return template(Method.GET, SERVICE_PATH + (nameOrId == null ? "" : nameOrId) + "/routes",
        RoutePageResp.class);
  }

  public RouteResp updateRoute(String id, RouteReq req) throws IOException {
    return template(Method.PATCH, ROUTE_PATH + (id == null ? "" : id), req, RouteResp.class);
  }

  public RouteResp updateOrCreateRoute(String id, RouteReq req) throws IOException {
    return template(Method.PUT, ROUTE_PATH + (id == null ? "" : id), req, RouteResp.class);
  }


  public BaseResp deleteRoute(String id) throws IOException {
    return template(Method.DELETE, ROUTE_PATH + (id == null ? "" : id), BaseResp.class);
  }

  public RoutePageResp listRoutesNext(String next) throws IOException {
    return template(Method.GET, next, RoutePageResp.class);
  }

  public BaseResp deleteAllRoutes() throws IOException {
    List<String> ids = new ArrayList<String>();
    RoutePageResp pageResp = listRoutes(null, 1000);

    if (pageResp.getData().size() == 0) {
      return BaseResp.noContent();
    }

    for (RouteResp resp : pageResp.getData()) {
      ids.add(resp.getId());
    }

    while (pageResp.getNext() != null) {
      pageResp = listRoutesNext(pageResp.getNext());
      for (RouteResp resp : pageResp.getData()) {
        ids.add(resp.getId());
      }
    }

    BaseResp resp = null;
    for (String id : ids) {
      resp = deleteRoute(id);
      if (!resp.succeed()) {
        break;
      }
    }

    return resp;
  }

  public BaseResp deleteRoutesByService(String nameOrId) throws IOException {
    List<String> ids = new ArrayList<String>();
    RoutePageResp pageResp = listRoutesByService(nameOrId);

    if (pageResp.getData().size() == 0) {
      return BaseResp.noContent();
    }

    for (RouteResp resp : pageResp.getData()) {
      ids.add(resp.getId());
    }

    while (pageResp.getNext() != null) {
      pageResp = listRoutesNext(pageResp.getNext());
      for (RouteResp resp : pageResp.getData()) {
        ids.add(resp.getId());
      }
    }

    BaseResp resp = null;
    for (String id : ids) {
      resp = deleteRoute(id);
      if (!resp.succeed()) {
        break;
      }
    }
    return resp;
  }


  /***************************************  Consumer ***********************************************************/

  public ConsumerResp createConsumer(ConsumerReq req) throws IOException {
    return template(Method.POST, CONSUMER_PATH, req, ConsumerResp.class);
  }

  public ConsumerResp retrieveConsumer(String usernameOrId) throws IOException {
    return template(Method.GET, CONSUMER_PATH + (usernameOrId == null ? "" : usernameOrId),
        ConsumerResp.class);
  }

  public ConsumerPageResp listConsumers() throws IOException {
    return template(Method.GET, CONSUMER_PATH, ConsumerPageResp.class);
  }

  public ConsumerResp updateConsumer(String usernameOrId, ConsumerReq req) throws IOException {
    return template(Method.PATCH, CONSUMER_PATH + (usernameOrId == null ? "" : usernameOrId), req,
        ConsumerResp.class);
  }

  public ConsumerResp updateOrCreateConsumer(String usernameOrId, ConsumerReq req)
      throws IOException {
    return template(Method.PUT, CONSUMER_PATH + (usernameOrId == null ? "" : usernameOrId), req,
        ConsumerResp.class);
  }

  public BaseResp deleteConsumer(String usernameOrId) throws IOException {
    return template(Method.DELETE, CONSUMER_PATH + (usernameOrId == null ? "" : usernameOrId),
        BaseResp.class);
  }


  /***************************************  Plugin ***********************************************************/

  public PluginResp addPlugin(PluginReq req) throws IOException {
    return template(Method.POST, PLUGIN_PATH, req, PluginResp.class);
  }

  public PluginResp retrievePlugin(String id) throws IOException {
    return template(Method.GET, PLUGIN_PATH + (id == null ? "" : id), PluginResp.class);
  }

  public PluginPageResp listPlugins(PluginQuery query) throws IOException {
    return template(Method.GET, PLUGIN_PATH + query.toString(), PluginPageResp.class);
  }

  public PluginResp updatePlugin(String id, PluginReq req) throws IOException {
    return template(Method.PATCH, PLUGIN_PATH + (id == null ? "" : id), req, PluginResp.class);
  }

  public PluginResp updateOrAddPlugin(PluginReq req) throws IOException {
    return template(Method.PUT, PLUGIN_PATH, req, PluginResp.class);
  }

  public BaseResp deletePlugin(String id) throws IOException {
    return template(Method.DELETE, PLUGIN_PATH + (id == null ? "" : id), BaseResp.class);
  }

  public EnabledPluginsResp retrieveEnabledPlugins() throws IOException {
    return template(Method.GET, PLUGIN_PATH + "enabled", EnabledPluginsResp.class);
  }

  public PluginSchemaResp retrievePluginSchema(String name) throws IOException {
    return template(Method.GET, PLUGIN_PATH + "schema/" + (name == null ? "" : name),
        PluginSchemaResp.class);
  }


  /***************************************  Certificate ***********************************************************/

  public CertificateResp addCertificate(CertificateReq req) throws IOException {
    return template(Method.POST, CERTIFICATE_PATH, req, CertificateResp.class);
  }

  public CertificatePageResp listCertificates() throws IOException {
    return template(Method.GET, CERTIFICATE_PATH, CertificatePageResp.class);
  }

  public CertificateResp retrieveCertificate(String sniOrId) throws IOException {
    return template(Method.GET, CERTIFICATE_PATH + (sniOrId == null ? "" : sniOrId),
        CertificateResp.class);
  }

  public CertificateResp updateCertificate(String sniOrId, CertificateReq req) throws IOException {
    return template(Method.PATCH, CERTIFICATE_PATH + (sniOrId == null ? "" : sniOrId), req,
        CertificateResp.class);
  }

  public CertificateResp updateOrCreateCertificate(String sniOrId, CertificateReq req)
      throws IOException {
    return template(Method.PUT, CERTIFICATE_PATH + (sniOrId == null ? "" : sniOrId), req,
        CertificateResp.class);
  }

  public BaseResp deleteCertificate(String sniOrId) throws IOException {
    return template(Method.DELETE, CERTIFICATE_PATH + (sniOrId == null ? "" : sniOrId),
        BaseResp.class);
  }


  /***************************************  SNI ***********************************************************/

  public SNIResp addSNI(SNIReq req) throws IOException {
    return template(Method.POST, SNI_PATH, req, SNIResp.class);
  }

  public SNIResp retrieveSNI(String nameOrId) throws IOException {
    return template(Method.GET, SNI_PATH + (nameOrId == null ? "" : nameOrId), SNIResp.class);
  }

  public SNIPageResp listSNIs() throws IOException {
    return template(Method.GET, SNI_PATH, SNIPageResp.class);
  }

  public SNIResp updateSNI(String nameOrId, SNIReq req) throws IOException {
    return template(Method.PATCH, SNI_PATH + (nameOrId == null ? "" : nameOrId), req,
        SNIResp.class);
  }

  public SNIResp updateOrCreateSNI(String nameOrId, SNIReq req) throws IOException {
    return template(Method.PUT, SNI_PATH + (nameOrId == null ? "" : nameOrId), req, SNIResp.class);
  }

  public BaseResp deleteSNI(String nameOrId) throws IOException {
    return template(Method.DELETE, SNI_PATH + (nameOrId == null ? "" : nameOrId), BaseResp.class);
  }


  /***************************************  Upstream ***********************************************************/

  public UpstreamResp addUpstream(UpstreamReq req) throws IOException {
    return template(Method.POST, UPSTREAM_PATH, req, UpstreamResp.class);
  }

  public UpstreamResp retrieveUpstream(String nameOrId) throws IOException {
    return template(Method.GET, UPSTREAM_PATH + (nameOrId == null ? "" : nameOrId),
        UpstreamResp.class);
  }

  public UpstreamPageResp listUpstreams() throws IOException {
    return template(Method.GET, UPSTREAM_PATH, UpstreamPageResp.class);
  }

  public UpstreamResp updateUpstream(String nameOrId, UpstreamReq req) throws IOException {
    return template(Method.PATCH, UPSTREAM_PATH + (nameOrId == null ? "" : nameOrId), req,
        UpstreamResp.class);
  }

  public UpstreamResp updateOrCreateUpstream(String nameOrId, UpstreamReq req) throws IOException {
    return template(Method.PUT, UPSTREAM_PATH + (nameOrId == null ? "" : nameOrId), req,
        UpstreamResp.class);
  }

  public BaseResp deleteUpstream(String nameOrId) throws IOException {
    return template(Method.DELETE, UPSTREAM_PATH + (nameOrId == null ? "" : nameOrId),
        BaseResp.class);
  }

  public UpstreamResp retrieveUpstreamByTarget(String hostPortOrId) throws IOException {
    return template(Method.GET,
        TARGET_PATH + (hostPortOrId == null ? "" : hostPortOrId) + "/upstream", UpstreamResp.class);
  }

  public EndpointPageResp endpoint(String nameOrId) throws IOException {
    return template(Method.GET, UPSTREAM_PATH + (nameOrId == null ? "" : nameOrId) + "/health/",
        EndpointPageResp.class);
  }


  /***************************************  Target ***********************************************************/

  public TargetResp addTarget(String id, TargetReq req) throws IOException {
    return template(Method.POST, UPSTREAM_PATH + (id == null ? "" : id) + TARGET_PATH, req,
        TargetResp.class);
  }

  public TargetPageResp listTargets(String nameOrId, TargetQuery query) throws IOException {
    return template(Method.GET,
        UPSTREAM_PATH + (nameOrId == null ? "" : nameOrId) + TARGET_PATH + (query == null ? ""
            : query.toString()), TargetPageResp.class);
  }

  public TargetPageResp listAllTargets(String nameOrId) throws IOException {
    return template(Method.GET,
        UPSTREAM_PATH + (nameOrId == null ? "" : nameOrId) + "/targets/all/", TargetPageResp.class);
  }

  public boolean containsTargetIncludeWeight0(String upstreamNameOrId, String target) throws IOException {
    TargetPageResp pageResp = listAllTargets(upstreamNameOrId);

    if (pageResp.getData().size() == 0) {
      return false;
    }

    for (TargetResp resp : pageResp.getData()) {
      if (resp.getTarget().equals(target)) {
        return true;
      }
    }

    return false;
  }

  public boolean containsTarget(String upstreamNameOrId, String target) throws IOException {
    TargetPageResp pageResp = listTargets(upstreamNameOrId, null);

    if (pageResp.getData().size() == 0) {
      return false;
    }

    for (TargetResp resp : pageResp.getData()) {
      if (resp.getTarget().equals(target)) {
        return true;
      }
    }

    return false;
  }

  public BaseResp deleteTarget(String upsteamNameOrId, String targetOrId) throws IOException {
    return template(Method.DELETE,
        UPSTREAM_PATH + (upsteamNameOrId == null ? "" : upsteamNameOrId) + TARGET_PATH + (
            targetOrId == null ? "" : targetOrId), BaseResp.class);
  }

  public BaseResp setTargetAsHealthy(String upsteamNameOrId, String targetOrId) throws IOException {
    return template(Method.POST,
        UPSTREAM_PATH + (upsteamNameOrId == null ? "" : upsteamNameOrId) + TARGET_PATH + (
            targetOrId == null ? "" : targetOrId) + "/healthy", BaseResp.class);
  }

  public BaseResp setTargetAsUnhealthy(String upsteamNameOrId, String targetOrId)
      throws IOException {
    return template(Method.POST,
        UPSTREAM_PATH + (upsteamNameOrId == null ? "" : upsteamNameOrId) + TARGET_PATH + (
            targetOrId == null ? "" : targetOrId) + "/unhealthy", BaseResp.class);
  }

  /***************************************  KeyAuth ***********************************************************/


  public KeyAuthResp createKeyAuth(String consumerNameOrId, KeyAuthReq req) throws IOException {
    return template(Method.POST,
        CONSUMER_PATH + (consumerNameOrId == null ? "" : consumerNameOrId) + "/key-auth", req,
        KeyAuthResp.class);
  }

  public KeyAuthPageResp listKeyAuths(String consumerNameOrId) throws IOException {
    return template(Method.GET,
        CONSUMER_PATH + (consumerNameOrId == null ? "" : consumerNameOrId) + "/key-auth",
        KeyAuthPageResp.class);
  }
}
