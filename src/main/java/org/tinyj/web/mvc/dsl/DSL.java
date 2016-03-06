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
package org.tinyj.web.mvc.dsl;

import org.tinyj.web.mvc.*;
import org.tinyj.web.mvc.filter.HttpRequestFilter;
import org.tinyj.web.mvc.filter.HttpRequestFilterChain;
import org.tinyj.web.mvc.render.BinaryRenderer;
import org.tinyj.web.mvc.render.Streamer;
import org.tinyj.web.mvc.render.TextRenderer;
import org.tinyj.web.mvc.render.Texter;
import org.tinyj.web.mvc.resource.HttpResource;
import org.tinyj.web.mvc.resource.WebMVCResource;
import org.tinyj.web.mvc.route.HttpRequestDispatcher;
import org.tinyj.web.mvc.route.WebMVCRequestDispatcher;

import java.io.InputStream;
import java.io.Reader;
import java.util.function.Function;

import static java.util.Arrays.asList;

/**
 * Helper methods to define a domain specific language to simplify creating
 * Request filter chains, routing tables and handlers.
 */
public interface DSL {


  /** Shortcut for `new MVCBridge(view, controller)`. */
  static <X> HttpRequestHandler mvc(WebView<X> view, WebController<X> controller) {
    return new WebMVCBridge<>(view, controller);
  }

  /**
   * Bridge for the view-controller pattern, where, instead of a model, the
   * `controller` returns a renderer prepared to render the response.
   */
  static <X extends WebRenderer> HttpRequestHandler vc(WebController<X> controller) {
    return new WebMVCBridge<>(WebRenderer::render, controller);
  }


  /*/
  #### (#HttpRequestDispatcher) and (#WebMVCRequestDispatcher) factories
  /*/

  /** Dispatch requests based on their path info. See (#HttpRequestDispatcher). */
  static HttpRequestHandler dispatch(HttpRequestDispatcher.Route... routes) {
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
  static <X> HttpRequestDispatcher.Route mvc(String target, WebView<X> view, WebController<X> controller) {
    return new HttpRequestDispatcher.Route(target, new WebMVCBridge<>(view, controller));
  }

  /** Shortcut for `route(target, vc(controller))`. */
  static <X extends WebRenderer> HttpRequestDispatcher.Route vc(String target, WebController<X> controller) {
    return new HttpRequestDispatcher.Route(target, new WebMVCBridge<>(WebRenderer::render, controller));
  }

  /** Shortcut for `route(target, dispatch(routes))`. */
  static HttpRequestDispatcher.Route dispatch(String target, HttpRequestDispatcher.Route... routes) {
    return new HttpRequestDispatcher.Route(target, new HttpRequestDispatcher(routes));
  }

  /** Dispatch requests based on their path info. See (#WebMVCRequestDispatcher) */
  @SafeVarargs
  static <X> WebController<X> dispatch(WebMVCRequestDispatcher.Route<? extends X>... routes) {
    return new WebMVCRequestDispatcher<>(routes);
  }

  /** Creates a dispatcher routing entry */
  static <X> WebMVCRequestDispatcher.Route<X> route(String path, WebController<X> controller) {
    return new WebMVCRequestDispatcher.Route<>(path, controller);
  }

  /** Shortcut for `route(target, controller(methods))`. */
  @SafeVarargs
  static <X> WebMVCRequestDispatcher.Route<X> controller(String path, WebMVCResource.Method<? extends X>... methods) {
    return new WebMVCRequestDispatcher.Route<>(path, new WebMVCResource<X>(methods));
  }

  /** Shortcut for `route(target, dispatch(routes))`. */
  @SafeVarargs
  static <X> WebMVCRequestDispatcher.Route<X> dispatch(String path, WebMVCRequestDispatcher.Route<? extends X>... routes) {
    return new WebMVCRequestDispatcher.Route<>(path, new WebMVCRequestDispatcher<>(routes));
  }


  /*/
  #### (#HttpResource) and (#WebController) factories
  /*/

  /** Shortcut for `new HttpResource(handlers)`, see (#HttpResource) */
  static HttpResource resource(HttpResource.Method... handlers) {
    return new HttpResource(handlers);
  }

  /** Shortcut for `new WebMVCResource(handlers)`, see (#WebMVCResource) */
  @SafeVarargs
  static <X> WebMVCResource controller(WebMVCResource.Method<? extends X>... handlers) {
    return new WebMVCResource<>(handlers);
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
  #### (#WebMVCResource.Method) factories
  /*/

  /** Shortcut for `new WebMVCResource.Method(method, handler)`. */
  static <X> WebMVCResource.Method<X> method(String method, WebController<X> handler) {
    return new WebMVCResource.Method<>(method, handler);
  }

  /** Shortcut for `new WebMVCResource.Method("DELETE", handler)`. */
  static <X> WebMVCResource.Method<X> delete(WebController<X> handler) {
    return new WebMVCResource.Method<>("DELETE", handler);
  }

  /** Shortcut for `new WebMVCResource.Method("GET", handler)`. */
  static <X> WebMVCResource.Method<X> get(WebController<X> handler) {
    return new WebMVCResource.Method<>("GET", handler);
  }

  /** Shortcut for `new WebMVCResource.Method("HEAD", handler)`. */
  static <X> WebMVCResource.Method<X> head(WebController<X> handler) {
    return new WebMVCResource.Method<>("HEAD", handler);
  }

  /** Shortcut for `new WebMVCResource.Method("OPTIONS", handler)`. */
  static <X> WebMVCResource.Method<X> options(WebController<X> handler) {
    return new WebMVCResource.Method<>("OPTIONS", handler);
  }

  /** Shortcut for `new WebMVCResource.Method("PATCH", handler)`. */
  static <X> WebMVCResource.Method<X> patch(WebController<X> handler) {
    return new WebMVCResource.Method<>("PATCH", handler);
  }

  /** Shortcut for `new WebMVCResource.Method("POST", handler)`. */
  static <X> WebMVCResource.Method<X> post(WebController<X> handler) {
    return new WebMVCResource.Method<>("POST", handler);
  }

  /** Shortcut for `new WebMVCResource.Method("PUT", handler)`. */
  static <X> WebMVCResource.Method<X> put(WebController<X> handler) {
    return new WebMVCResource.Method<>("PUT", handler);
  }


  /*/
  #### (#WebView) and (#WebRenderer) factories
  /*/

  /**
   * creates a View that applies the renderer provided by `rendererFactory` for
   * the model to the response.
   */
  static <X> WebView<X> view(Function<? super X, ? extends WebRenderer> rendererFactory) {
    return (x, res) -> rendererFactory.apply(x).render(res);
  }

  /** Renderer using `streamer` to stream data to the response output stream. */
  static BinaryRenderer streamWith(Streamer streamer) {
    return new BinaryRenderer(streamer);
  }

  /** Renderer streaming data from `inputStream` to the response output stream. */
  static BinaryRenderer streamFrom(InputStream inputStream) {
    return new BinaryRenderer(inputStream);
  }

  /** Renderer using `texter` to write to the response writer. */
  static TextRenderer writeWith(Texter texter) {
    return new TextRenderer(texter);
  }

  /** Renderer writing data from `reader` to the response writer. */
  static TextRenderer readFrom(Reader reader) {
    return new TextRenderer(reader);
  }

  /**
   * Renderer writing `toWrite` converted into a string using `stringifier` to
   * the response writer.
   */
  static <T> TextRenderer write(T toWrite, Function<? super T, String> stringifier) {
    return new TextRenderer(writer -> writer.write(stringifier.apply(toWrite)));
  }

  /**
   * Renderer writing `toWrite` converted into a string using `toWrite.toString`
   * to the response writer.
   */
  static TextRenderer write(Object toWrite) {
    return new TextRenderer(writer -> writer.write(toWrite.toString()));
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
}
