/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.caller;

import com.taofen8.mid.kong.caller.exception.CallerException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class CocoRealCall implements CocoCall {

  final CocoRequest originalRequest;
  final CocoClient client;


  public CocoRealCall(CocoClient client, CocoRequest request) {
    this.client = client;
    this.originalRequest = request;
  }


  public CocoResponse execute() throws CallerException {
    Request request = null;
    CocoResponse response = null;
    int maxRetryTimes = 3;
    int retryCount = 0;
    while (retryCount < maxRetryTimes) {
      request = this.originalRequest.buildOkHttpRequest(client);
      Response okResponse = null;
      try {
        okResponse = client.httpClient().newCall(request).execute();
        response = new CocoResponse(okResponse, client);
      } catch (SocketTimeoutException e) {
        retryCount++;
        continue;
      } catch (IOException e) {
        response = new CocoResponse(503, e.getMessage() + ",url=" + request.url());
      }
      break;
    }
    return response;
  }

  public void enqueue(CocoCallback callback) throws CallerException {
    final CocoCallback finalCallback = callback;
    Request request = this.originalRequest.buildOkHttpRequest(client);
    final CocoRealCall that = this;

    client.httpClient().newCall(request).enqueue(
        new okhttp3.Callback() {
          public void onFailure(Call call, IOException e) {
            finalCallback.onFailure(that, e);
          }

          public void onResponse(Call call, Response response) throws IOException {
            finalCallback.onResponse(that, new CocoResponse(response, that.client));
          }
        }
    );
  }
}
