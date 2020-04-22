/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin;

import com.taofen8.mid.kong.admin.query.PluginQuery;
import com.taofen8.mid.kong.admin.request.CertificateReq;
import com.taofen8.mid.kong.admin.request.ConsumerReq;
import com.taofen8.mid.kong.admin.request.KeyAuthReq;
import com.taofen8.mid.kong.admin.request.PluginReq;
import com.taofen8.mid.kong.admin.request.RouteReq;
import com.taofen8.mid.kong.admin.request.SNIReq;
import com.taofen8.mid.kong.admin.request.ServiceReq.Builder;
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
import com.taofen8.mid.kong.admin.response.RoutePageResp;
import com.taofen8.mid.kong.admin.response.RouteResp;
import com.taofen8.mid.kong.admin.response.SNIPageResp;
import com.taofen8.mid.kong.admin.response.SNIResp;
import com.taofen8.mid.kong.admin.response.ServiceResp;
import com.taofen8.mid.kong.admin.response.TargetPageResp;
import com.taofen8.mid.kong.admin.response.TargetResp;
import com.taofen8.mid.kong.admin.response.UpstreamPageResp;
import com.taofen8.mid.kong.admin.response.UpstreamResp;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class JKongAdminTest {

  private String adminUrl = "http://192.168.0.1:8001";


  @Test
  public void testAddService() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    ServiceResp resp = admin.addService(new Builder()
        .host("default.unreach")
        .name("kong.test.gw-hello.test6.name")
        .path("/timout")
        .build());
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testDeleteService() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    BaseResp resp = admin.deleteService("e4dd03ff-735b-4346-8616-9477abafaa93");
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testAddRoute() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    RouteResp resp = admin
        .updateOrCreateRoute("f056c60f-2012-4d83-bb52-caffde70e572", new RouteReq.Builder()
            .path("/admin/records")
            .method("POST")
            .build()
        );
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());

  }


  @Test
  public void testListRoutes() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    RoutePageResp resp = admin.listRoutes(null, 1000);
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testListRoutesByService() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    RoutePageResp resp = admin.listRoutesByService("265cb303-8430-4159-a5ba-dbc0594116c8");
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testUpdateRoute() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    RouteResp resp = admin.updateRoute("ff43abaf-df2f-488a-82b6-bc24a22b875b",
        new RouteReq.Builder()
            .preserveHost(true)
            .method("POST")
            .method("GET")
            .build()
    );
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }


  @Test
  public void testDeleteRoutesByService() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    BaseResp resp = admin.deleteRoutesByService("ebb9c46e-83f0-42af-8741-c266c3bb9ef0");
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testDeleteAllRoutes() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    BaseResp resp = admin.deleteAllRoutes();
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testDeleteRoute() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    BaseResp resp = admin.deleteRoute("506a8b4c-8566-4eda-9905-4ddcc4085d95");
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testRetrieveConsumer() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    ConsumerResp resp = admin.retrieveConsumer("test-user");
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testListConsumers() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    ConsumerPageResp resp = admin.listConsumers();
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testUpdateConsumer() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    ConsumerResp resp = admin.updateConsumer("b70cdc73-66bc-4a08-b8f8-3fc2edf06709",
        new ConsumerReq.Builder()
            .customId("android")
            .build());
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testUpdateOrCreateConsumer() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    ConsumerResp resp = admin.updateOrCreateConsumer("b70cdc73-66bc-4a08-b8f8-3fc2edf06709",
        new ConsumerReq.Builder()
            .customId("android")
            .build());
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testDeleteConsumer() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    BaseResp resp = admin.deleteConsumer("b70cdc73-66bc-4a08-b8f8-3fc2edf06709");
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testAddPlugin() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);

    BaseResp resp = admin.addPlugin(new PluginReq.Builder()
        .name("tfg-adaptor")
        .enabled(true)
        .build());
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testRetrievePlugin() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    PluginResp resp = admin.retrievePlugin("b37f87dd-2705-420f-9631-a9f2c9c76451");
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testListPlugins() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    PluginPageResp resp = admin.listPlugins(new PluginQuery.Builder()
        .build());
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testUpdatePlugin() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    List paths = new ArrayList<String>();
    List ips = new ArrayList<String>();

    PluginResp resp = admin.updatePlugin("b37f87dd-2705-420f-9631-a9f2c9c76451",
        new PluginReq.Builder()
            .enabled(false)
            .config("forward_urls", paths)
            .config("forward_ips_except", ips)
            .config("force_forward", false)
            .build()
    );
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testUpdateOrAddPlugin() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    PluginResp resp = admin.updateOrAddPlugin(new PluginReq.Builder()
        .name("key-auth")
        .enabled(false)
        .build()
    );
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testDeletePlugin() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    BaseResp resp = admin.deletePlugin("76bd056a-c066-4678-919a-5a1827c37fd0");
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testRetrieveEnabledPlugins() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    EnabledPluginsResp resp = admin.retrieveEnabledPlugins();
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }


  @Test
  public void testAddCertificate() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    CertificateResp resp = admin.addCertificate(new CertificateReq.Builder()
        .cert("xxx")
        .cert("xxx")
        .build());
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testRetrieveCertificate() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    CertificateResp resp = admin.retrieveCertificate("");
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testListcertificates() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    CertificatePageResp resp = admin.listCertificates();
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testUpdateCertificate() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    CertificateResp resp = admin.updateCertificate("", new CertificateReq.Builder().build());
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testUpdateOrCreateCertificate() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    CertificateResp resp = admin
        .updateOrCreateCertificate("", new CertificateReq.Builder().build());
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }


  @Test
  public void testAddSNI() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    SNIResp resp = admin.addSNI(new SNIReq.Builder().bulid());
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testRetrieveSNI() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    SNIResp resp = admin.retrieveSNI("");
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testListSNIs() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    SNIPageResp resp = admin.listSNIs();
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testUpdateSNI() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    SNIResp resp = admin.updateSNI("", new SNIReq.Builder().bulid());
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testUpdateOrCreateSNI() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    SNIResp resp = admin.updateOrCreateSNI("", new SNIReq.Builder().bulid());
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testDeleteSNI() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    BaseResp resp = admin.deleteSNI("");
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testListUpstreams() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    UpstreamPageResp resp = admin.listUpstreams();
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testRetrieveUpstreamByTarget() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    UpstreamResp resp = admin.retrieveUpstreamByTarget("target1:8000");
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testUpdateUpstream() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    UpstreamResp resp = admin.updateUpstream("upstream_api", new UpstreamReq.Builder()
        .slots(22)
        .build());
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testCotainsTarget() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    boolean contains = admin
        .containsTarget("ba4ccac7-8d9c-4d05-b282-e0e113c29852", "192.168.0.120:8800");
    Assert.assertTrue(contains);
    System.out.println(contains);
  }


  @Test
  public void testEndpoint() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    EndpointPageResp resp = admin.endpoint("haihu-api");
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testAddTarget() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    TargetResp resp = admin.addTarget("test-upstreamapi2", new TargetReq.Builder()
        .target("target2")
        .weight(200)
        .build());
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }


  @Test
  public void testListAllTargets() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    TargetPageResp resp = admin.listAllTargets("apis.haihu.gw");
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testDeleteTarget() throws IOException {
    //JKongAdmin admin = new JKongAdmin("http://122.225.102.162",9877);
    JKongAdmin admin = new JKongAdmin(adminUrl);
    BaseResp resp = admin.deleteTarget("apis.haihu.gw", "192.168.0.120:8800");
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testSetTargetAsHealthy() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    BaseResp resp = admin.setTargetAsHealthy("upstream_api", "target1:8000");
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testSetTargetAsUnhealthy() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    BaseResp resp = admin
        .setTargetAsUnhealthy("upstream_api", "d0ec59f3-43ac-48fa-a74c-7acf2cd7f058");
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testCreateKeyAuth() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    KeyAuthResp resp = admin.createKeyAuth("test-user", new KeyAuthReq());
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }

  @Test
  public void testRetrieveKeyAuth() throws IOException {
    JKongAdmin admin = new JKongAdmin(adminUrl);
    KeyAuthPageResp resp = admin.listKeyAuths("test1-user");
    Assert.assertTrue(resp.succeedOrIngoreConflict());
    System.out.println(resp.toString());
  }


}
