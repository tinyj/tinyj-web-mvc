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

import java.io.InputStream;
import java.io.Reader;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static org.tinyj.web.mvc.render.Texter.textFrom;

/**
 * Helper methods to define a domain specific language to simplify creating
 * Request filter chains, routing tables and handlers.
 */
public interface DSL {

  /*/
  #### (#HttpRequestHandler) factories
  /*/

  /** Shortcut for `new MVCBridge(view, controller)`. */
  static <X> HttpRequestHandler mvc(WebView<? super X> view, WebController<? extends X> controller) {
    return ((request, response) -> view.render(controller.handle(request), request, response));
  }

  /** Shortcut for `mvc(view, controller(methods)). */
  @SafeVarargs
  static <X> HttpRequestHandler mvc(WebView<? super X> view, WebResource.Method<? extends X>... methods) {
    final WebResource<? extends X> controller = controller(methods);
    return ((request, response) -> view.render(controller.handle(request), request, response));
  }

  /** Shortcut for `new HttpResource(handlers)`, see (#HttpResource) */
  static HttpResource resource(HttpResource.Method... handlers) {
    return new HttpResource(handlers);
  }

  /** Shortcut for `new WebResource(methods)`, see (#WebResource) */
  @SafeVarargs
  static <X> WebResource<X> controller(WebResource.Method<? extends X>... methods) {
    return new WebResource<>(methods);
  }

  /*/
  #### (#HttpResource.Method) factories
  /*/

  /** Shortcut for `new HttpResource.Method(method, handler)`. */
  static HttpResource.Method method(String method, HttpRequestHandler handler) {
    return new HttpResource.Method(method, handler);
  }

  /** Shortcut for `new HttpResource.Method("DELETE", handler)`. */
  static HttpResource.Method delete(HttpRequestHandler handler) {
    return new HttpResource.Method("DELETE", handler);
  }

  /** Shortcut for `new HttpResource.Method("GET", handler)`. */
  static HttpResource.Method get(HttpRequestHandler handler) {
    return new HttpResource.Method("GET", handler);
  }

  /** Shortcut for `new HttpResource.Method("HEAD", handler)`. */
  static HttpResource.Method head(HttpRequestHandler handler) {
    return new HttpResource.Method("HEAD", handler);
  }

  /** Shortcut for `new HttpResource.Method("OPTIONS", handler)`. */
  static HttpResource.Method options(HttpRequestHandler handler) {
    return new HttpResource.Method("OPTIONS", handler);
  }

  /** Shortcut for `new HttpResource.Method("PATCH", handler)`. */
  static HttpResource.Method patch(HttpRequestHandler handler) {
    return new HttpResource.Method("PATCH", handler);
  }

  /** Shortcut for `new HttpResource.Method("POST", handler)`. */
  static HttpResource.Method post(HttpRequestHandler handler) {
    return new HttpResource.Method("POST", handler);
  }

  /** Shortcut for `new HttpResource.Method("PUT", handler)`. */
  static HttpResource.Method put(HttpRequestHandler handler) {
    return new HttpResource.Method("PUT", handler);
  }


  /*/
  #### (#WebResource.Method) factories
  /*/

  /** Shortcut for `new WebResource.Method(method, handler)`. */
  static <X> WebResource.Method<X> method(String method, WebController<X> handler) {
    return new WebResource.Method<>(method, handler);
  }

  /** Shortcut for `new WebResource.Method("DELETE", handler)`. */
  static <X> WebResource.Method<X> delete(WebController<X> handler) {
    return new WebResource.Method<>("DELETE", handler);
  }

  /** Shortcut for `new WebResource.Method("GET", handler)`. */
  static <X> WebResource.Method<X> get(WebController<X> handler) {
    return new WebResource.Method<>("GET", handler);
  }

  /** Shortcut for `new WebResource.Method("HEAD", handler)`. */
  static <X> WebResource.Method<X> head(WebController<X> handler) {
    return new WebResource.Method<>("HEAD", handler);
  }

  /** Shortcut for `new WebResource.Method("OPTIONS", handler)`. */
  static <X> WebResource.Method<X> options(WebController<X> handler) {
    return new WebResource.Method<>("OPTIONS", handler);
  }

  /** Shortcut for `new WebResource.Method("PATCH", handler)`. */
  static <X> WebResource.Method<X> patch(WebController<X> handler) {
    return new WebResource.Method<>("PATCH", handler);
  }

  /** Shortcut for `new WebResource.Method("POST", handler)`. */
  static <X> WebResource.Method<X> post(WebController<X> handler) {
    return new WebResource.Method<>("POST", handler);
  }

  /** Shortcut for `new WebResource.Method("PUT", handler)`. */
  static <X> WebResource.Method<X> put(WebController<X> handler) {
    return new WebResource.Method<>("PUT", handler);
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

  /** Shortcut for `route(target, resource(methods))`. */
  static HttpRequestDispatcher.Route resource(String target, HttpResource.Method... methods) {
    return new HttpRequestDispatcher.Route(target, new HttpResource(methods));
  }

  /** Shortcut for `route(target, mvc(view, controller))`. */
  static <X> HttpRequestDispatcher.Route mvc(String target, WebView<? super X> view, WebController<X> controller) {
    return new HttpRequestDispatcher.Route(
        target, (request, response) -> view.render(controller.handle(request), request, response));
  }

  /** Shortcut for `route(target, mvc(view, controller(methods)))`. */
  @SafeVarargs
  static <X> HttpRequestDispatcher.Route mvc(String target, WebView<? super X> view, WebResource.Method<? extends X>... methods) {
    final WebResource<X> controller = controller(methods);
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

  /** Shortcut for `route(target, controller(methods))`. */
  @SafeVarargs
  static <X> WebMVCRequestDispatcher.Route<X> controller(String path, WebResource.Method<? extends X>... methods) {
    return new WebMVCRequestDispatcher.Route<>(path, new WebResource<X>(methods));
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

  /** Shortcut for `new HttpRequestFilterChain(chainedFilters)`. */
  static HttpRequestFilter filter(HttpRequestFilter... chainedFilters) {
    return chain(chainedFilters);
  }

  /** Alias for `filter(HttpRequestFilter...)`. */
  static HttpRequestFilter chain(HttpRequestFilter... chainedFilters) {
    return new HttpRequestFilterChain(asList(chainedFilters));
  }


  /*/
  ### (#HttpRenderer) factories
  /*/

  /** Renderer using `streamer` to stream data to the response output stream. */
  static HttpRenderer streamUsing(Streamer streamer) {
    return new BinaryRenderer(streamer);
  }

  /** Renderer streaming data from `inputStream` to the response output stream. */
  static HttpRenderer streamFrom(InputStream inputStream) {
    return new BinaryRenderer(Streamer.streamFrom(inputStream));
  }

  /** Renderer using `texter` to write to the response writer. */
  static HttpRenderer writeUsing(Texter texter) {
    return new TextRenderer(texter);
  }

  /** Renderer writing text from `reader` to the response writer. */
  static HttpRenderer writeFrom(Reader reader) {
    return new TextRenderer(textFrom(reader));
  }

  /**
   * Renderer writing `toWrite` converted into a string using `stringifier` to
   * the response writer.
   */
  static <T> HttpRenderer writeFrom(T toWrite, Function<? super T, String> stringifier) {
    return new TextRenderer(textFrom(toWrite, stringifier));
  }

  /**
   * Renderer writing `toWrite` converted into a string using `toWrite.toString`
   * to the response writer.
   */
  static HttpRenderer writeFrom(Object toWrite) {
    return new TextRenderer(textFrom(toWrite));
  }
}
