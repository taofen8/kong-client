/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.executor;

import com.taofen8.mid.kong.executor.SingleTask.ICallBack;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingleExecutor {

  private static Logger logger = LoggerFactory.getLogger(SingleExecutor.class);
  private static ExecutorService executorService = Executors.newSingleThreadExecutor();

  private SingleExecutor() {
  }

  public static void submit(SingleTask singleTask) {
    Callable callable = singleTask.getCallable();
    if (callable == null) {
      return;
    }
    Future future = executorService.submit(singleTask.getCallable());
    try {
      Object v = future.get();
      ICallBack callBack = singleTask.getCallBack();
      if (callBack != null) {
        callBack.callBack(v);
      }
    } catch (InterruptedException e) {
      logger.error("SingleExecutor InterruptedException error,", e);
      Thread.currentThread().interrupt();
    } catch (ExecutionException e) {
      logger.error("SingleExecutor ExecutionException error,", e);
    }
  }
}
