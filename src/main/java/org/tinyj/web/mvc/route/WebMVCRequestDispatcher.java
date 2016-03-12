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

import org.tinyj.web.mvc.WebController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

/**
 * Dispatches `HttpServletRequest` matching the request's pathInfo against a set
 * of routes.
 */
public class WebMVCRequestDispatcher<X> implements WebController<X> {

  protected final HttpRequestRouter<WebController<? extends X>> router;

  @SafeVarargs
  public WebMVCRequestDispatcher(final Route<? extends X>... routes) {
    router = new HttpRequestRouter<>();
    setRoutes(routes);
  }

  protected WebMVCRequestDispatcher() {
    router = new HttpRequestRouter<>();
  }

  @SafeVarargs
  protected final void setRoutes(Route<? extends X>... routes) {
    Map<String, WebController<? extends X>> routingTable = stream(routes).collect(toMap(r -> r.route, r -> r.target));
    router.setRoutes(routingTable);
  }

  @Override
  public X handle(HttpServletRequest request) throws Exception {
    HttpRequestRouter.Routing<WebController<? extends X>> routing = router.route(request);
    return routing.target.handle(routing.routedRequest);
  }

  public static class Route<X> {

    public final String route;
    public final WebController<X> target;

    public Route(String target, WebController<X> routed) {
      this.route = target;
      this.target = routed;
    }
  }
}
