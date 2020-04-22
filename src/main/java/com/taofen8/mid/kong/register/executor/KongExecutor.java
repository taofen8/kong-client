/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.register.executor;

import com.taofen8.mid.kong.admin.request.RouteReq;
import com.taofen8.mid.kong.admin.request.ServiceReq;
import com.taofen8.mid.kong.admin.request.TargetReq;
import com.taofen8.mid.kong.admin.request.UpstreamReq.Builder;
import com.taofen8.mid.kong.admin.response.BaseResp;
import com.taofen8.mid.kong.admin.response.RoutePageResp;
import com.taofen8.mid.kong.admin.response.ServiceResp;
import com.taofen8.mid.kong.admin.response.UpstreamResp;
import com.taofen8.mid.kong.register.struct.RequestMethod;
import com.taofen8.mid.kong.admin.JKongAdmin;
import com.taofen8.mid.kong.register.struct.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

public class KongExecutor extends ThreadPoolExecutor {

  private static int MAX_BLOCKING_SIZE = 2000;

  private CountDownLatch latch;

  private static Logger logger = LoggerFactory.getLogger(
      KongExecutor.class);

  private JKongAdmin admin;
  private Map<String, Object> lockFactory = new HashMap<>();

  public KongExecutor(int corePoolSize, int maximumPoolSize, long keepActiveTime,
      TimeUnit timeunit) {
    super(corePoolSize, maximumPoolSize, keepActiveTime, timeunit,
        new LinkedBlockingQueue<Runnable>(MAX_BLOCKING_SIZE));
  }

  public void setAdminUrl(String adminUrl) {
    this.admin = new JKongAdmin(adminUrl);
  }

  public boolean isCompleted() {
    try {
      latch.await();
    } catch (InterruptedException e) {
      logger.error(this.getClass() + " isCompleted InterruptedException", e);
      Thread.currentThread().interrupt();
    }
    return true;
  }

  public void submit(List<Service> services) {
    latch = new CountDownLatch(services.size());

    for (final Service service : services) {
      super.execute(new Runnable() {
        @Override
        public void run() {
          try {
            BaseResp resp = null;
            do {
              do {
                if (!lockFactory.containsKey(service.upstream().name())) {
                  synchronized (service.upstream().name()) {
                    if (!lockFactory.containsKey(service.upstream().name())) {
                      lockFactory.put(service.upstream().name(), null);
                    } else {
                      break;
                    }
                  }
                } else {
                  break;
                }

                /**
                 * register upstream, this maybe returned 201 created or 409 conflict.
                 */
                resp = admin.updateOrCreateUpstream(
                    service.upstream().name()
                    , new Builder()
                        .name(service.upstream().name())
                        .algorithm(service.upstream().algorithm())
                        .hashOn(service.upstream().hashOn())
                        .hashFallback(service.upstream().hashFallback())
                        .hashOnHeader(service.upstream().hashOnHeader())
                        .hashFallbackHeader(service.upstream().hashFallbackHeader())
                        .hashOnCookie(service.upstream().hashOnCookie())
                        .hashOnCookiePath(service.upstream().hashOnCookiePath())
                        .slots(service.upstream().slots())
                        .tags(service.upstream().tags())
                        .hostHeader(service.upstream().hostHeader())
                        .healthchecks(service.upstream().healthcheckConfig())
                        .build()
                );

                if (!resp.succeedOrIngoreConflict()) {
                  break;
                }

                /**
                 * add target to upstream, the same ip and port will be added only once.
                 */
                String upstreamId = ((UpstreamResp) resp).getId();

                if (!admin.containsTarget(upstreamId,
                    service.upstream().target().target())) {
                  resp = admin.addTarget(upstreamId,
                      new TargetReq.Builder()
                          .target(service.upstream().target().target())
                          .weight(service.upstream().target().weight())
                          .tags((service.upstream().target().tags()))
                          .build()
                  );
                  if (!resp.succeed()) {
                    break;
                  }
                }
                logger.info("register upstream [{}] succeed with target [{}],serviceName[{}]",
                    service.upstream().name(),
                    service.upstream().target().target(), service.name());
              } while (false);

              if (resp != null && !resp.succeed()) {
                logger.error("register upstream [{}] failed by: {}", service.name(),
                    resp);
              }

              resp = admin.updateOrCreateService(service.name(),
                  new ServiceReq.Builder()
                      .name(service.name())
                      .retries(service.retries())
                      .protocol(service.protocol())
                      .host(service.host())
                      .port(service.port())
                      .path(service.path())
                      .connectTimeout(service.connectTimeout())
                      .writeTimeout(service.writeTimeout())
                      .readTimeout(service.readTimeout())
                      .tags(service.tags())
                      .url(service.url())
                      .build()
              );

              if (!resp.succeed()) {
                logger
                    .error("register service [{}] failed by: {}", service.name(), resp);
                break;
              }

              logger.info("register service succeed: [{}]  ", service.name());

              String serviceId = ((ServiceResp) resp).getId();
              RoutePageResp routePage = admin.listRoutesByService(serviceId);

              List<String> methods = new ArrayList<>(4);
              for (RequestMethod method : service.route().methods()) {
                methods.add(method.name());
              }

              RouteReq routeReq = new RouteReq.Builder()
                  .name(service.route().name())
                  .protocols(service.route().protocols())
                  .methods(methods)
                  .hosts(service.route().hosts())
                  .paths(service.route().paths())
                  .headers(service.route().headers())
                  .httpsRedirectStatusCode(service.route().httpsRedirectStatusCode())
                  .regexPriority(service.route().regexPriority())
                  .stripPath(service.route().stripPath())
                  .pathHandling(service.route().pathHandling())
                  .preserveHost(service.route().preserveHost())
                  .snis(service.route().snis())
                  .sources(service.route().sources())
                  .destinations(service.route().destinations())
                  .tags(service.route().tags())
                  .serviceId(serviceId)
                  .build();

              String routeId = null;
              if (CollectionUtils.isEmpty(routePage.getData())) {
                resp = admin.addRoute(routeReq);
              } else {
                routeId = routePage.getData().get(0).getId();
                resp = admin.updateOrCreateRoute(routeId, routeReq);
              }

              if (!resp.succeed()) {
                logger.error("register route [{}] failed by: {}", service.name(),
                    resp);
                break;
              }

              logger.info("register route succeed: [{}]  ", service.route().paths());
            } while (false);
          } catch (IOException e) {
            logger.error("register service [{}] failed by: {}", service.name(), e);
          } finally {
            latch.countDown();
          }
        }
      });
    }
  }


}
