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

import org.tinyj.web.mvc.HttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

/**
 * Dispatches `HttpServletRequest` matching the request's pathInfo against a set
 * of routes.
 */
public class HttpRequestDispatcher implements HttpRequestHandler {

  protected final HttpRequestRouter<HttpRequestHandler> router;

  public HttpRequestDispatcher(Route... routes) {
    Map<String, HttpRequestHandler> routingTable = stream(routes).collect(toMap(r -> r.route, r -> r.target));
    routingTable.putIfAbsent("/*", this::notFound);
    router = new HttpRequestRouter<>(routingTable);
  }

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
    HttpRequestRouter.Routing<HttpRequestHandler> routing = router.route(request);
    routing.target.handle(routing.routedRequest, response);
  }

  protected void notFound(HttpServletRequest request, HttpServletResponse response) throws Exception {
    response.setStatus(404);
    response.getWriter().append("Not found!\n").flush();
  }

  public static class Route {

    public final String route;
    public final HttpRequestHandler target;

    public Route(String target, HttpRequestHandler routed) {
      this.route = target;
      this.target = routed;
    }
  }
}
