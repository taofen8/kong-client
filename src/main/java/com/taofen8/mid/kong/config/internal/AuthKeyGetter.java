/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.config.internal;

import com.taofen8.mid.kong.executor.SingleTask.ICallBack;
import com.taofen8.mid.kong.admin.JKongAdmin;
import com.taofen8.mid.kong.admin.request.ConsumerReq;
import com.taofen8.mid.kong.admin.request.KeyAuthReq;
import com.taofen8.mid.kong.admin.response.ConsumerResp;
import com.taofen8.mid.kong.admin.response.KeyAuthPageResp;
import com.taofen8.mid.kong.admin.response.KeyAuthResp;
import com.taofen8.mid.kong.config.internal.InternalConfig.InternalConfigEntry;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class AuthKeyGetter implements Callable<String> {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private String adminUrl;
  private String appIdentifier;

  public AuthKeyGetter(String adminUrl, String appIdentifier) {
    this.adminUrl = adminUrl;
    this.appIdentifier = appIdentifier;
  }

  @Override
  public String call() throws Exception {
    JKongAdmin kongAdmin = new JKongAdmin(adminUrl);

    ConsumerResp resp = kongAdmin.retrieveConsumer(appIdentifier);
    String consumerId = null;
    if (resp.succeed()) {
      consumerId = resp.getId();
    }

    if (StringUtils.isEmpty(consumerId)) {
      ConsumerReq req = new ConsumerReq.Builder().username(appIdentifier).build();
      ConsumerResp consumerResp = kongAdmin.createConsumer(req);
      if (consumerResp.succeed() && !StringUtils.isEmpty(consumerResp.getId())) {
        consumerId = consumerResp.getId();
      } else {
        logger.error("can not create consumer {} by: {}", appIdentifier, consumerResp);
      }
    }

    KeyAuthPageResp keyAuthPageResp = kongAdmin.listKeyAuths(consumerId);
    if (keyAuthPageResp == null || CollectionUtils.isEmpty(keyAuthPageResp.getData())) {
      KeyAuthResp keyAuthResp = kongAdmin.createKeyAuth(consumerId, new KeyAuthReq());
      if (keyAuthResp.succeed()) {
        return keyAuthResp.getKey();
      }
    } else {
      return keyAuthPageResp.getData().get(0).getKey();
    }
    return null;
  }

  public static class AuthKeyGetterCallBack implements ICallBack<String> {

    private InternalConfig internalConfig;

    public AuthKeyGetterCallBack(InternalConfig internalConfig) {
      this.internalConfig = internalConfig;
    }

    @Override
    public void callBack(String s) {
      internalConfig.configure(InternalConfigEntry.COMSUMER_KEY, s);
    }
  }
}
