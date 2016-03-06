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
package org.tinyj.web.mvc.resource;

import org.tinyj.web.mvc.HttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

/**
 * Dispatches a request to a HTTP resource to a set of (#MethodHandlers).
 *
 * `HttpResource` comes with three default handlers:
 *
 * - OPTIONS requests are answered with the _Allow_ header set to the list of
 * supported methods.
 * - if a GET handler is registered, HEAD requests are dispatched to the GET
 * handler but the response body is discarded.
 * - if fallback (*) is registered, requests for unregistered requests are
 * answered with "405 Method not allowed!". The _Allow_ header set to the list
 * of supported methods.
 */
public class HttpResource implements HttpRequestHandler {

  protected final Map<String, HttpRequestHandler> methods = new HashMap<>();
  protected final HttpRequestHandler fallback;

  /**
   * Create new `HttpResource` dispatching requests to `handlers`. A handler
   * for `*` is removed from the list an registered as fallback handler.
   *
   * If `handlers` contains multiple handlers for the same method the later wins.
   */
  public HttpResource(MethodHandler... handlers) {
    methods.putAll(stream(handlers).collect(toMap(MethodHandler::method, MethodHandler::handler)));
    methods.putIfAbsent("OPTIONS", this::options);
    if (methods.containsKey("GET")) {
      methods.putIfAbsent("HEAD", this::head);
    }
    HttpRequestHandler fallback = methods.remove("*");
    this.fallback = fallback != null ? fallback : this::methodNotAllowed;
  }

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
    methods.getOrDefault(request.getMethod(), fallback)
        .handle(request, response);
  }

  /**
   * returns the list of supported methods for the default implementations of the
   * OPTIONS handler and the fallback handler.
   *
   * @return the supported Methods
   */
  protected Set<String> supportedMethods() {
    return methods.keySet();
  }

  /** default OPTIONS method handler */
  protected void options(HttpServletRequest request, HttpServletResponse response) {
    response.setHeader("Allow", String.join(",", supportedMethods()));
  }

  /** default HEAD method handler */
  protected void head(HttpServletRequest request, HttpServletResponse response) throws Exception {
    HeadResponse headResponse = new HeadResponse(response);
    methods.get("GET").handle(request, headResponse);
    headResponse.close();
  }

  /** default method handler fallback */
  protected void methodNotAllowed(HttpServletRequest request, HttpServletResponse response) throws Exception {
    response.setStatus(405);
    response.setHeader("Allow", String.join(",", supportedMethods()));
    response.getWriter().append("Method not allowed!\n").flush();
  }

  public static class MethodHandler {

    public final String method;
    public final HttpRequestHandler handler;

    public MethodHandler(String method, HttpRequestHandler handler) {
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
