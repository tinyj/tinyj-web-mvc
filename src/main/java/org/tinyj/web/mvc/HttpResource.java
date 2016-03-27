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
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

/**
 * Dispatches a request to a HTTP resource to a set of method handlers.
 */
public class HttpResource implements HttpRequestHandler {

  protected final Map<String, HttpRequestHandler> methods = new HashMap<>();
  protected HttpRequestHandler fallback;

  /**
   * Create new `HttpResource` dispatching requests to `handlers`. A handler
   * for `*` is removed from the list an registered as fallback handler.
   *
   * If `handlers` contains multiple handlers for the same method the later wins.
   */
  public HttpResource(Method... handlers) {
    setMethods(handlers);
  }

  protected HttpResource() {
    fallback = this::methodNotAllowed;
  }

  /** Register method handlers. */
  protected final void setMethods(Method... handlers) {
    methods.putAll(stream(handlers).collect(toMap(Method::method, Method::handler)));
    methods.putIfAbsent("OPTIONS", this::options);
    HttpRequestHandler fallback = methods.remove("*");
    this.fallback = fallback != null ? fallback : this::methodNotAllowed;
  }

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
    methods.getOrDefault(request.getMethod(), fallback)
        .handle(request, response);
  }

  /** Default OPTIONS method handler. */
  protected void options(HttpServletRequest request, HttpServletResponse response) {
    response.setHeader("Allow", String.join(",", supportedMethods()));
  }

  /** Default method handler fallback, throws (#MethodNotAllowedException). */
  protected void methodNotAllowed(HttpServletRequest request, HttpServletResponse response) throws Exception {
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

  public static class Method {

    public final String method;
    public final HttpRequestHandler handler;

    public Method(String method, HttpRequestHandler handler) {
      this.method = method;
      this.handler = handler;
    }

    public String method() {
      return method;
    }

    public HttpRequestHandler handler() {
      return handler;
    }
  }
}
