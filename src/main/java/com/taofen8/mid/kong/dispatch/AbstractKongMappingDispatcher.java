/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.dispatch;

import com.taofen8.mid.kong.dispatch.exception.KongConfigException;
import com.taofen8.mid.kong.dispatch.handler.KongRequest;
import com.taofen8.mid.kong.dispatch.handler.KongResponse;
import com.taofen8.mid.kong.dispatch.handler.RequestBodyIntercepter;
import com.taofen8.mid.kong.dispatch.handler.RequestHandler;
import com.taofen8.mid.kong.dispatch.handler.ResponseBodyIntercepter;
import com.taofen8.mid.kong.dispatch.handler.ResponseHandler;
import com.taofen8.mid.kong.dispatch.handler.impl.RequestGzipIntercepter;
import com.taofen8.mid.kong.register.ServiceRegistration;
import com.taofen8.mid.kong.register.internal.KongMappingRegistration;
import com.taofen8.mid.kong.register.struct.MicroService;
import com.taofen8.mid.kong.register.struct.Service;
import com.taofen8.mid.kong.spring.BeanFactory;
import com.taofen8.mid.kong.util.JsonUtil;
import com.taofen8.mid.kong.config.Config;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public abstract class AbstractKongMappingDispatcher<R, S> extends AbstractKongDispatcher {

  private static Logger logger = LoggerFactory.getLogger(AbstractKongMappingDispatcher.class);

  private List<RequestHandler<R>> requestHandlers;
  private List<ResponseHandler<R, S>> responseHandlers;
  private List<RequestBodyIntercepter> requestBodyIntercepters;
  private List<ResponseBodyIntercepter> responseBodyIntercepters;
  private ServiceRegistration serviceRegistry = new KongMappingRegistration();
  private boolean enableOptionsRequestProcessor = false;
  private OptionsProcessor optionsProcessor;
  private String charset = "UTF-8";


  public void setEnableOptionsRequestProcessor(boolean enableOptionsRequestProcessor) {
    this.enableOptionsRequestProcessor = enableOptionsRequestProcessor;
  }

  /**
   * 请求数据前置处理
   *
   * @return
   */
  protected List<RequestHandler<R>> createRequestHandlers() {
    return new ArrayList<RequestHandler<R>>(0);
  }

  /**
   * 请求返回后置处理
   *
   * @return
   */
  protected List<ResponseHandler<R, S>> createResponseHandlers() {
    return new ArrayList<ResponseHandler<R, S>>(0);
  }

  /**
   * @return
   */
  protected List<RequestBodyIntercepter> createRequestBodyIntercepters() {
    return new ArrayList<RequestBodyIntercepter>(0);
  }

  /**
   * @return
   */
  protected List<ResponseBodyIntercepter> createResonseBodyIntercepters() {
    return new ArrayList<ResponseBodyIntercepter>(0);
  }

  /**
   * 服务调用前的拦截
   *
   * @param request
   */
  protected void beforeInvoke(R request) throws Exception {
  }

  /**
   * 服务调用后的处理
   *
   * @param request
   * @param response
   */
  protected void afterInvoke(R request, S response) throws Exception {
  }

  @Override
  public ServiceRegistration getServiceRegistration() {
    return serviceRegistry;
  }

  /**
   * 解析请求参数转换
   *
   * @param request
   * @param context
   * @return
   */
  protected KongRequest<R> transformRequest(HttpServletRequest request,
      RequestContext<R, S> context) throws Exception {
    Map<String, String> requestHeaders = new HashMap<>();
    Map<String, Cookie> requestCookies = new HashMap<>();

    Cookie[] cookies = request.getCookies();
    if (cookies != null && cookies.length > 0) {
      for (Cookie cookie : cookies) {
        requestCookies.put(cookie.getName(), cookie);
      }
    }

    Enumeration<String> headerNames = request.getHeaderNames();
    String headerName = null;
    while (headerNames.hasMoreElements()) {
      headerName = headerNames.nextElement();
      requestHeaders.put(headerName, request.getHeader(headerName));
    }

    KongRequest kongRequest = new KongRequest.Builder<R>().requestHeaders(requestHeaders)
        .requestCookies(requestCookies).serviceMapping(context.getServiceMapping()).build();

    R r = resolveRequestBody(request, context, kongRequest);

    return new KongRequest.Builder<R>().requestBody(r).requestHeaders(kongRequest.requestHeaders())
        .requestCookies(kongRequest.requestCookies()).serviceMapping(kongRequest.serviceMapping())
        .build();
  }

  protected R resolveRequestBody(HttpServletRequest request,
      RequestContext<R, S> context, KongRequest kongRequest) throws Exception {
    byte[] bytesOfBody = readRequestBody(request);

    if (requestBodyIntercepters != null) {
      for (RequestBodyIntercepter intercepter : requestBodyIntercepters) {
        bytesOfBody = intercepter.intercept(bytesOfBody, kongRequest);
      }
    }

    Map json = JsonUtil.parseObject(new String(bytesOfBody, charset), Map.class);
    if (json == null) {
      json = new HashMap<String, Object>(4);
    }
    Enumeration<String> paramterNames = request.getParameterNames();
    String pName = null;
    while (paramterNames.hasMoreElements()) {
      pName = paramterNames.nextElement();
      json.put(pName, request.getParameter(pName));
    }
    R r = JsonUtil.parseObject(JsonUtil.toJSONString(json), context.getRequestClass());
    return r;
  }

  private byte[] readRequestBody(HttpServletRequest request) {

    try (
        InputStream is = request.getInputStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ) {
      byte[] buffer = new byte[1024];
      int len = 0;
      while ((len = is.read(buffer)) != -1) {
        bos.write(buffer, 0, len);
      }
      bos.close();
      byte[] data = bos.toByteArray();
      return data;

    } catch (IOException e) {
      logger.error("readRequestBody error:", e);
    }

    return new byte[0];
  }

  /**
   * 返回数据转换
   *
   * @param response
   * @param context
   * @return
   */
  protected KongResponse<S> transformResponse(S response, RequestContext<R, S> context)
      throws Exception {
    return new KongResponse.Builder<S>().responseBody(response)
        .serviceMapping(context.getServiceMapping()).build();
  }

  /**
   * 异常处理
   *
   * @param kongRequest
   * @param e
   * @return
   */
  protected KongResponse<S> exceptionHandler(KongRequest<R> kongRequest, Exception e,
      RequestContext<R, S> context) {
    if (kongRequest != null && logger.isInfoEnabled()) {
      logger.info(JsonUtil.toJSONString(kongRequest));
    }
    return new KongResponse.Builder<S>().status(500).serviceMapping(context.getServiceMapping()).
        errorMessage(e.getMessage()).build();
  }

  public void init(ApplicationContext context, Config config) throws KongConfigException {
    requestHandlers = createRequestHandlers();
    responseHandlers = createResponseHandlers();
    requestBodyIntercepters = createRequestBodyIntercepters();
    responseBodyIntercepters = createResonseBodyIntercepters();

    if (requestBodyIntercepters == null) {
      requestBodyIntercepters = new ArrayList<>(2);
    }
    requestBodyIntercepters.add(0, new RequestGzipIntercepter());

    if (enableOptionsRequestProcessor) {
      optionsProcessor = BeanFactory
          .getBeanForTypeIncludingAncestors(context, OptionsProcessor.class);
      if (optionsProcessor == null) {
        throw new KongConfigException(
            "enableOptionsRequestProcessor is set true, but not provide a implement of OptionsProcessor");
      }
    }

  }


  @Override
  public boolean kongDispatch(Service service, HttpServletRequest request,
      HttpServletResponse response) {
    MicroService serviceLocal = (MicroService) service;
    RequestContext context = new RequestContext.Builder()
        .requestClass(serviceLocal.invocation().signature().requestType())
        .responseClass(serviceLocal.invocation().signature().responseType())
        .serviceMapping(serviceLocal.serviceMapping()).build();

    if (enableOptionsRequestProcessor && optionsProcessor
        .processOptions(request, response, service)) {
      return true;
    }

    KongRequest<R> kongRequest = null;
    KongResponse<S> kongResponse = null;
    boolean error = false;
    try {
      //拼装请求对象
      kongRequest = transformRequest(request, context);
      if (!CollectionUtils.isEmpty(requestHandlers)) {
        for (RequestHandler<R> handler : requestHandlers) {
          handler.handle(kongRequest);
        }
      }

      R req = kongRequest.requestBody();
      if (req == null) {
        req = (R) context.getRequestClass()
            .newInstance();
      }

      //执行请求
      this.beforeInvoke(req);
      S invResponse = (S) serviceLocal.invocation().invoke(req);
      if (invResponse == null) {
        logger.warn("service method [{}]  does not have a response",
            serviceLocal.invocation().signature().methodName());
      }

      //请求后置
      this.afterInvoke(req, invResponse);

      //拼装返回信息
      kongResponse = this
          .transformResponse(invResponse, context);
      if (!CollectionUtils.isEmpty(responseHandlers)) {
        for (ResponseHandler<R, S> handler : responseHandlers) {
          handler.handle(kongRequest, kongResponse);
        }
      }
    } catch (Exception e) {
      logger.error("kong dispatch error: ", e);
      kongResponse = exceptionHandler(kongRequest, e, context);
      if (null == kongResponse) {
        kongResponse = new KongResponse.Builder<S>()
            .serviceMapping(context.getServiceMapping()).status(500).errorMessage(e.getMessage())
            .build();
      }
    } finally {
      try {
        writeResponse(kongRequest, kongResponse, response);
      } catch (Exception e) {
        logger.error("writeResponse error by:", e);
      }
    }
    return true;
  }

  protected void writeResponse(KongRequest<R> kongRequest, KongResponse<S> kongResponse,
      HttpServletResponse response) throws Exception {
    response.setStatus(kongResponse.status());

    try {
      byte[] bytes = null;
      if (kongResponse.status() == 200) {
        String result = "";
        if (kongResponse.responseBody() != null) {
          result = JsonUtil.toJSONString(kongResponse.responseBody());
        }

        bytes = result.getBytes(charset);
        if (responseBodyIntercepters != null) {
          for (ResponseBodyIntercepter intercepter : responseBodyIntercepters) {
            bytes = intercepter.intercept(bytes, kongRequest, kongResponse);
          }
        }
      } else {
        String result = "";
        if (!StringUtils.isEmpty(kongResponse.errorMessage())) {
          result = kongResponse.errorMessage();
        }
        bytes = result.getBytes(charset);
      }

      setHeadersAndCookies(kongResponse, response);
      response.getOutputStream().write(bytes);
    } catch (IOException e) {
      logger.error("kong writeResponse error:", e);
    }
  }


  private void setHeadersAndCookies(KongResponse<S> kongResponse,
      HttpServletResponse response) {
    Iterator<Entry<String, String>> respHeadIterator = kongResponse.responseHeaders().entrySet()
        .iterator();
    while (respHeadIterator.hasNext()) {
      Entry<String, String> entry = respHeadIterator.next();
      response.setHeader(entry.getKey(), entry.getValue());
    }

    Iterator<Entry<String, Cookie>> cookieIterator = kongResponse.responseCookies().entrySet()
        .iterator();
    while (cookieIterator.hasNext()) {
      response.addCookie(cookieIterator.next().getValue());
    }
  }


}
