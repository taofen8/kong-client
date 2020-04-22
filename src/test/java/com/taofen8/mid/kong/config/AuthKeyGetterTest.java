/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.config;

import com.taofen8.mid.kong.config.internal.InternalConfig;
import com.taofen8.mid.kong.config.internal.InternalConfig.InternalConfigEntry;
import com.taofen8.mid.kong.config.internal.AuthKeyGetter;
import com.taofen8.mid.kong.config.internal.AuthKeyGetter.AuthKeyGetterCallBack;
import com.taofen8.mid.kong.executor.SingleTask;
import org.junit.Assert;
import org.junit.Test;

public class AuthKeyGetterTest {


  @Test
  public void testAuthKeyGetter() throws InterruptedException {

    InternalConfig config = new InternalConfig();
    new SingleTask<String>(
        new AuthKeyGetter("http://192.168.0.5:8001", "test-user"),
        new AuthKeyGetterCallBack(config));

    //A sleep is needed here .Thread.sleep(5000);
    Assert.assertNotNull(InternalConfigEntry.COMSUMER_KEY);
  }


}
