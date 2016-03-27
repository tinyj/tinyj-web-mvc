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
package org.tinyj.web.mvc;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

/**
 * Dispatches a request to a HTTP resource to a set of method handlers.
 */
public class WebResource<X> implements WebController<X> {

  protected final Map<String, WebController<? extends X>> methods = new HashMap<>();
  protected WebController<? extends X> fallback;

  /**
   * Create new `WebResource` dispatching requests to `handlers`. A handler
   * for `*` is used as fallback handler.
   *
   * If `handlers` contains multiple handlers for the same method the last one is used.
   */
  @SafeVarargs
  public WebResource(final MethodHandler<? extends X>... handlers) {
    setMethods(handlers);
  }

  protected WebResource() {
    fallback = this::methodNotAllowed;
  }

  /** Register method handlers. */
  @SafeVarargs
  protected final void setMethods(MethodHandler<? extends X>... handlers) {
    this.methods.clear();
    methods.putAll(stream(handlers).collect(toMap(MethodHandler::method, MethodHandler::handler)));
    WebController<? extends X> fallback = methods.remove("*");
    this.fallback = fallback != null ? fallback : this::methodNotAllowed;
  }

  @Override
  public X handle(HttpServletRequest request) throws Exception {
    return methods.getOrDefault(request.getMethod(), fallback)
        .handle(request);
  }

  /** Default method handler fallback, throws (#MethodNotAllowedException). */
  protected X methodNotAllowed(HttpServletRequest request) {
    throw new MethodNotAllowedException(supportedMethods());
  }

  /**
   * Returns the list of supported methods.
   *
   * @return the supported Methods
   */
  protected Set<String> supportedMethods() {
    return methods.keySet();
  }

  public static class MethodHandler<X> {

    public final String method;
    public final WebController<X> handler;

    public MethodHandler(String method, WebController<X> handler) {
      this.method = method;
      this.handler = handler;
    }

    public String method() {
      return method;
    }

    public WebController<X> handler() {
      return handler;
    }
  }
}
