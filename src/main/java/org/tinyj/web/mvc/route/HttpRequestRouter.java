/*
Copyright 2016 Eric Karge <e.karge@struction.de>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.tinyj.web.mvc.route;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.tinyj.web.mvc.route.RoutedHttpRequest.routedRequest;

public class HttpRequestRouter<T> {

  protected final Map<String, T> routes = new HashMap<>();

  public HttpRequestRouter() {
  }

  public HttpRequestRouter(Map<String, T> routes) {
    this.routes.putAll(routes);
  }

  public void setRoutes(Map<String, T> routes) {
    this.routes.clear();
    this.routes.putAll(routes);
  }

  public Routing<T> route(final HttpServletRequest request) {
    String prefix = request.getPathInfo();
    prefix = prefix != null ? prefix : "";
    T target = routes.get(prefix);
    if (target != null) {
      return new Routing<>(request, prefix, target);
    }
    while (true) {
      target = routes.get(prefix + "/*");
      if (target != null) {
        return new Routing<>(request, prefix, target);
      }
      if (prefix.isEmpty()) {
        throw new NoSuchElementException(request.getPathInfo());
      }
      prefix = prefix.substring(0, prefix.lastIndexOf('/'));
    }
  }

  public static class Routing<T> {

    public final HttpServletRequest routedRequest;
    public final T target;

    public Routing(HttpServletRequest request, String path, T target) {
      this.routedRequest = routedRequest(request, path);
      this.target = target;
    }
  }
}
