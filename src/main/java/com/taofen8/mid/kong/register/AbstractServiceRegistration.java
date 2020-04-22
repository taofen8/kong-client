/*
 * Copyright(c) 2020 taofen8.com. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package com.taofen8.mid.kong.register;

import com.taofen8.mid.kong.config.Config;
import com.taofen8.mid.kong.register.struct.Service;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationContext;

public abstract class AbstractServiceRegistration<T extends Service> implements
    ServiceRegistration {

  private Map<String, T> serviceMap = new HashMap<String, T>();

  protected abstract Map<String, T> collectServices(ApplicationContext context,
      Config config);

  protected abstract String getServiceKey(HttpServletRequest request);

  public T findService(HttpServletRequest request) {
    return this.serviceMap.get(getServiceKey(request));
  }

  @Override
  public void registerServices(ApplicationContext context, Config config) {
    this.serviceMap = collectServices(context, config);
  }

  @Override
  public List<T> getAllServices() {
    Collection<T> valueCollection = this.serviceMap.values();
    final int size = valueCollection.size();
    T[] servicesArray = (T[]) new Service[size];
    return Arrays.asList(serviceMap.values().toArray(servicesArray));
  }
}
