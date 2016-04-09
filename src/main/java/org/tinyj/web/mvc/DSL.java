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

import org.tinyj.web.mvc.render.*;
import org.tinyj.web.mvc.route.HttpRequestDispatcher;
import org.tinyj.web.mvc.route.WebMVCRequestDispatcher;

import static java.util.Arrays.asList;

/**
 * Helper methods to define a domain specific language to simplify creating
 * Request filter chains, routing tables and handlers.
 */
public interface DSL {

  /*/
  #### (#HttpRequestHandler) factories
  /*/

  /**
   * Returns a (#HttpRequestHandler) using `view` to render the model returned
   * by `controller`.
   */
  static <X> HttpRequestHandler mvc(WebView<? super X> view, WebController<? extends X> controller) {
    return ((request, response) -> view.render(controller.handle(request), request, response));
  }

  /** Shortcut for `mvc(view, controller(methodHandlers)). */
  @SafeVarargs
  static <X> HttpRequestHandler mvc(WebView<? super X> view,
                                    WebResource.MethodHandler<? extends X>... methodHandlers) {
    final WebResource<? extends X> controller = controller(methodHandlers);
    return ((request, response) -> view.render(controller.handle(request), request, response));
  }

  /** Shortcut for `new HttpResource(methodHandlers)`, see (#HttpResource) */
  static HttpResource resource(HttpResource.MethodHandler... methodHandlers) {
    return new HttpResource(methodHandlers);
  }

  /** Shortcut for `new WebController(methodHandlers)`, see (#WebController) */
  @SafeVarargs
  static <X> WebResource<X> controller(WebResource.MethodHandler<? extends X>... methodHandlers) {
    return new WebResource<>(methodHandlers);
  }

  /*/
  #### (#HttpResource.MethodHandler) factories
  /*/

  /** Shortcut for `new HttpResource.MethodHandler(method, handler)`. */
  static HttpResource.MethodHandler method(String method, HttpRequestHandler handler) {
    return new HttpResource.MethodHandler(method, handler);
  }

  /** Shortcut for `new HttpResource.MethodHandler("DELETE", handler)`. */
  static HttpResource.MethodHandler delete(HttpRequestHandler handler) {
    return new HttpResource.MethodHandler("DELETE", handler);
  }

  /** Shortcut for `new HttpResource.MethodHandler("GET", handler)`. */
  static HttpResource.MethodHandler get(HttpRequestHandler handler) {
    return new HttpResource.MethodHandler("GET", handler);
  }

  /** Shortcut for `new HttpResource.MethodHandler("HEAD", handler)`. */
  static HttpResource.MethodHandler head(HttpRequestHandler handler) {
    return new HttpResource.MethodHandler("HEAD", handler);
  }

  /** Shortcut for `new HttpResource.MethodHandler("OPTIONS", handler)`. */
  static HttpResource.MethodHandler options(HttpRequestHandler handler) {
    return new HttpResource.MethodHandler("OPTIONS", handler);
  }

  /** Shortcut for `new HttpResource.MethodHandler("PATCH", handler)`. */
  static HttpResource.MethodHandler patch(HttpRequestHandler handler) {
    return new HttpResource.MethodHandler("PATCH", handler);
  }

  /** Shortcut for `new HttpResource.MethodHandler("POST", handler)`. */
  static HttpResource.MethodHandler post(HttpRequestHandler handler) {
    return new HttpResource.MethodHandler("POST", handler);
  }

  /** Shortcut for `new HttpResource.MethodHandler("PUT", handler)`. */
  static HttpResource.MethodHandler put(HttpRequestHandler handler) {
    return new HttpResource.MethodHandler("PUT", handler);
  }


  /*/
  #### (#WebController.MethodHandler) factories
  /*/

  /** Shortcut for `new WebController.MethodHandler(method, handler)`. */
  static <X> WebResource.MethodHandler<X> method(String method, WebController<X> handler) {
    return new WebResource.MethodHandler<>(method, handler);
  }

  /** Shortcut for `new WebController.MethodHandler("DELETE", handler)`. */
  static <X> WebResource.MethodHandler<X> delete(WebController<X> handler) {
    return new WebResource.MethodHandler<>("DELETE", handler);
  }

  /** Shortcut for `new WebController.MethodHandler("GET", handler)`. */
  static <X> WebResource.MethodHandler<X> get(WebController<X> handler) {
    return new WebResource.MethodHandler<>("GET", handler);
  }

  /** Shortcut for `new WebController.MethodHandler("HEAD", handler)`. */
  static <X> WebResource.MethodHandler<X> head(WebController<X> handler) {
    return new WebResource.MethodHandler<>("HEAD", handler);
  }

  /** Shortcut for `new WebController.MethodHandler("OPTIONS", handler)`. */
  static <X> WebResource.MethodHandler<X> options(WebController<X> handler) {
    return new WebResource.MethodHandler<>("OPTIONS", handler);
  }

  /** Shortcut for `new WebController.MethodHandler("PATCH", handler)`. */
  static <X> WebResource.MethodHandler<X> patch(WebController<X> handler) {
    return new WebResource.MethodHandler<>("PATCH", handler);
  }

  /** Shortcut for `new WebController.MethodHandler("POST", handler)`. */
  static <X> WebResource.MethodHandler<X> post(WebController<X> handler) {
    return new WebResource.MethodHandler<>("POST", handler);
  }

  /** Shortcut for `new WebController.MethodHandler("PUT", handler)`. */
  static <X> WebResource.MethodHandler<X> put(WebController<X> handler) {
    return new WebResource.MethodHandler<>("PUT", handler);
  }


  /*/
  #### (#HttpRequestDispatcher) and (#WebMVCRequestDispatcher) factories
  /*/

  /** Dispatch requests based on their path info. See (#HttpRequestDispatcher). */
  static HttpRequestDispatcher dispatch(HttpRequestDispatcher.Route... routes) {
    return new HttpRequestDispatcher(routes);
  }

  /** Creates a dispatcher routing entry. */
  static HttpRequestDispatcher.Route route(String target, HttpRequestHandler handler) {
    return new HttpRequestDispatcher.Route(target, handler);
  }

  /** Shortcut for `route(target, resource(methodHandlers))`. */
  static HttpRequestDispatcher.Route resource(String target, HttpResource.MethodHandler... methodHandlers) {
    return new HttpRequestDispatcher.Route(target, new HttpResource(methodHandlers));
  }

  /** Shortcut for `route(target, mvc(view, controller))`. */
  static <X> HttpRequestDispatcher.Route mvc(String target, WebView<? super X> view, WebController<X> controller) {
    return new HttpRequestDispatcher.Route(
        target, (request, response) -> view.render(controller.handle(request), request, response));
  }

  /** Shortcut for `route(target, mvc(view, controller(methodHandlers)))`. */
  @SafeVarargs
  static <X> HttpRequestDispatcher.Route mvc(String target, WebView<? super X> view,
                                             WebResource.MethodHandler<? extends X>... methodHandlers) {
    final WebResource<X> controller = controller(methodHandlers);
    return new HttpRequestDispatcher.Route(
        target, (request, response) -> view.render(controller.handle(request), request, response));
  }

  /** Shortcut for `route(target, dispatch(routes))`. */
  static HttpRequestDispatcher.Route dispatch(String target, HttpRequestDispatcher.Route... routes) {
    return new HttpRequestDispatcher.Route(target, new HttpRequestDispatcher(routes));
  }

  /** Dispatch requests based on their path info. See (#WebMVCRequestDispatcher) */
  @SafeVarargs
  static <X> WebMVCRequestDispatcher<X> dispatch(WebMVCRequestDispatcher.Route<? extends X>... routes) {
    return new WebMVCRequestDispatcher<>(routes);
  }

  /** Creates a dispatcher routing entry */
  static <X> WebMVCRequestDispatcher.Route<X> route(String path, WebController<X> controller) {
    return new WebMVCRequestDispatcher.Route<>(path, controller);
  }

  /** Shortcut for `route(target, controller(methodHandlers))`. */
  @SafeVarargs
  static <X> WebMVCRequestDispatcher.Route<X> controller(String path,
                                                         WebResource.MethodHandler<? extends X>... methodHandlers) {
    return new WebMVCRequestDispatcher.Route<>(path, new WebResource<X>(methodHandlers));
  }

  /** Shortcut for `route(target, dispatch(routes))`. */
  @SafeVarargs
  static <X> WebMVCRequestDispatcher.Route<X> dispatch(String path, WebMVCRequestDispatcher.Route<? extends X>... routes) {
    return new WebMVCRequestDispatcher.Route<>(path, new WebMVCRequestDispatcher<>(routes));
  }


  /*/
  #### (#HttpRequestFilter) factories
  /*/

  /** Dummy to avoid defining empty filters. */
  static void filter() {
  }

  /** Shortcut to specify (#HttpRequestFilter) lambda style. Returns its argument. */
  static HttpRequestFilter filter(HttpRequestFilter filter) {
    return filter;
  }

  /**
   * Shortcut for `new HttpRequestFilterChain(chainedFilters)`,
   * see (#HttpRequestFilterChain).
   */
  static HttpRequestFilter filter(HttpRequestFilter... chainedFilters) {
    return new HttpRequestFilterChain(asList(chainedFilters));
  }

  /** Alias for `filter(HttpRequestFilter...)`. */
  static HttpRequestFilter chain(HttpRequestFilter... chainedFilters) {
    return new HttpRequestFilterChain(asList(chainedFilters));
  }


  /*/
  ### (#HttpRenderer) factories
  /*/

  /**
   * Renderer using `streamer` to stream data to the response output stream,
   * see (#BinaryRenderer).
   */
  static HttpRenderer streamResponseUsing(Streamer streamer) {
    return new BinaryRenderer(streamer);
  }

  /**
   * Renderer using `texter` to write to the response writer,
   * see (#TextRenderer).
   */
  static HttpRenderer writeResponseUsing(Texter texter) {
    return new TextRenderer(texter);
  }
}
