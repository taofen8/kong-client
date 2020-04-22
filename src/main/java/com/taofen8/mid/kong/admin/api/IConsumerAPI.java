/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.admin.api;

import com.taofen8.mid.kong.admin.response.BaseResp;
import com.taofen8.mid.kong.admin.response.ConsumerPageResp;
import com.taofen8.mid.kong.admin.response.ConsumerResp;
import com.taofen8.mid.kong.admin.request.ConsumerReq;
import java.io.IOException;

public interface IConsumerAPI {

  ConsumerResp createConsumer(ConsumerReq req) throws IOException;

  ConsumerResp retrieveConsumer(String usernameOrId) throws IOException;

  ConsumerPageResp listConsumers() throws IOException;

  ConsumerResp updateConsumer(String usernameOrId, ConsumerReq req) throws IOException;

  ConsumerResp updateOrCreateConsumer(String usernameOrId, ConsumerReq req) throws IOException;

  BaseResp deleteConsumer(String usernameOrId) throws IOException;



}
