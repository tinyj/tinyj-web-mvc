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
   * Create new `WebResource` dispatching requests to `handlers`. If a handler
   * for `*` is passed it's used as fallback handler. The default fallback is to
   * throw a (#MethodNotAllowedException).
   *
   * If `handlers` contains multiple handlers for the same method the later wins.
   */
  @SafeVarargs
  public WebResource(final Method<? extends X>... handlers) {
    setMethods(handlers);
  }

  /** Register method handlers. */
  @SafeVarargs
  protected final void setMethods(Method<? extends X>... handlers) {
    this.methods.clear();
    methods.putAll(stream(handlers).collect(toMap(Method::method, Method::controller)));
    WebController<? extends X> fallback = methods.remove("*");
    this.fallback = fallback != null ? fallback : this::methodNotAllowed;
  }

  protected WebResource() {
    fallback = this::methodNotAllowed;
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


  public static class Method<X> {

    public final String method;
    public final WebController<X> controller;

    public Method(String method, WebController<X> controller) {
      this.method = method;
      this.controller = controller;
    }

    public String method() {
      return method;
    }

    public WebController<X> controller() {
      return controller;
    }
  }
}
