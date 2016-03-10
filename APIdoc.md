## org.tinyj.web.mvc

### HttpRequestHandler
_[(src)](src/main/java/org/tinyj/web/mvc/HttpRequestHandler.java)_
_(interface)_

The `HttpRequestHandler` is the core abstraction for tinyj-web-mvc. A
 `HttpRequestHandler` is responsible for accepting a HTTP request and render
 an appropriate HTTP response.

**`handle(HttpServletRequest request, HttpServletResponse response)`**  
Use `response` to render a HTTP response to `request`.

 By passing `response` all claims onto `response` are yielded. I.e. after
 returning `response` may be in any state, it may or may not be committed,
 data may or may not been send.

 Even though it's not a strict requirement from this interface, most
 implementations will assume that prior to invocation `response` is uncommitted.

### WebController\<X>
_[(src)](src/main/java/org/tinyj/web/mvc/WebController.java)_
_(interface)_ | X: Type of the result model.

A conceptual variation of the [`HttpRequestHandler`](#httprequesthandler) interface. Instead of
 directly rendering into a passed `HttpServletResponse` output parameter a model
 describing the result is returned, leaving the final rendering to another party.

**`handle(HttpServletRequest request)`**  
⇒ *`X`* _(the result model)_  


### WebMVCBridge\<X>
_[(src)](src/main/java/org/tinyj/web/mvc/WebMVCBridge.java)_
X: Type of the result model.  
_implements_ [`HttpRequestHandler`](#httprequesthandler)

Bridges [`WebController`](#webcontrollerx) to [`HttpRequestHandler`](#httprequesthandler).

**`WebMVCBridge(WebView view, WebController handler)`** _(constructor)_  
Translate `handler` into a [`HttpRequestHandler`](#httprequesthandler) by using `view` to render
 the result model into a `HttpServletResponse`.

### WebRenderer
_[(src)](src/main/java/org/tinyj/web/mvc/WebRenderer.java)_
_(interface)_

`WebRender` is a conceptual variation of [`WebView`](#webviewx). Instead of returning a
 result model a [`WebController`](#webcontrollerx) returns a `WebRenderer` carrying all the state
 necessary to render a HTTP response.

 This allows for more control of the [`WebController`](#webcontrollerx) over the final HTTP response.

**`render(HttpServletResponse response)`**  
Render a HTTP response into `response`.

 By passing `response` all claims onto `response` are yielded. I.e. after
 returning `response` may be in any state, it may or may not be committed,
 data may or may not been send.

 Even though it's not a strict requirement from this interface, most
 implementations will assume that prior to invocation `response` is uncommitted.

### WebView\<X>
_[(src)](src/main/java/org/tinyj/web/mvc/WebView.java)_
_(interface)_ | X: model type.

A `WebView` is responsible for rendering the result model of an [`WebController`](#webcontrollerx)
 into a HTTP response.

**`render(X model, HttpServletResponse response)`**  
Render `model` into `response`.

 By passing `response` all claims onto `response` are yielded. I.e. after
 returning `response` may be in any state, it may or may not be committed,
 data may or may not been send.

 Even though it's not a strict requirement from this interface, most
 implementations will assume that prior to invocation `response` is uncommitted.

## org.tinyj.web.mvc.dsl

### DSL
_[(src)](src/main/java/org/tinyj/web/mvc/dsl/DSL.java)_
_(interface)_

Helper methods to define a domain specific language to simplify creating
 Request filter chains, routing tables and handlers.

**`mvc(WebView view, WebController controller)`**  
⇒ *`HttpRequestHandler`*  
Shortcut for `new MVCBridge(view, controller)`.

**`vc(WebController controller)`**  
⇒ *`HttpRequestHandler`*  
Bridge for the view-controller pattern, where, instead of a model, the
 `controller` returns a renderer prepared to render the response.

**`dispatch(Route[] routes)`**  
⇒ *`HttpRequestHandler`*  
Dispatch requests based on their path info. See [`HttpRequestDispatcher`](#httprequestdispatcher).

**`route(String target, HttpRequestHandler handler)`**  
⇒ *`HttpRequestDispatcher.Route`*  
Creates a dispatcher routing entry.

**`resource(String target, Method[] methods)`**  
⇒ *`HttpRequestDispatcher.Route`*  
Shortcut for `route(target, resource(methods))`.

**`mvc(String target, WebView view, WebController controller)`**  
⇒ *`HttpRequestDispatcher.Route`*  
Shortcut for `route(target, mvc(view, controller))`.

**`vc(String target, WebController controller)`**  
⇒ *`HttpRequestDispatcher.Route`*  
Shortcut for `route(target, vc(controller))`.

**`dispatch(String target, Route[] routes)`**  
⇒ *`HttpRequestDispatcher.Route`*  
Shortcut for `route(target, dispatch(routes))`.

**`dispatch(Route[] routes)`**  
⇒ *`WebController`*  
Dispatch requests based on their path info. See [`WebMVCRequestDispatcher`](#webmvcrequestdispatcherx)

**`route(String path, WebController controller)`**  
⇒ *`WebMVCRequestDispatcher.Route`*  
Creates a dispatcher routing entry

**`controller(String path, Method[] methods)`**  
⇒ *`WebMVCRequestDispatcher.Route`*  
Shortcut for `route(target, controller(methods))`.

**`dispatch(String path, Route[] routes)`**  
⇒ *`WebMVCRequestDispatcher.Route`*  
Shortcut for `route(target, dispatch(routes))`.

**`resource(Method[] handlers)`**  
⇒ *`HttpResource`*  
Shortcut for `new HttpResource(handlers)`, see [`HttpResource`](#httpresource)

**`controller(Method[] handlers)`**  
⇒ *`WebMVCResource`*  
Shortcut for `new WebMVCResource(handlers)`, see [`WebMVCResource`](#webmvcresourcex)

**`method(String method, HttpRequestHandler handler)`**  
⇒ *`HttpResource.Method`*  
Shortcut for `new HttpResource.Method(method, handler)`.

**`delete(HttpRequestHandler handler)`**  
⇒ *`HttpResource.Method`*  
Shortcut for `new HttpResource.Method("DELETE", handler)`.

**`get(HttpRequestHandler handler)`**  
⇒ *`HttpResource.Method`*  
Shortcut for `new HttpResource.Method("GET", handler)`.

**`head(HttpRequestHandler handler)`**  
⇒ *`HttpResource.Method`*  
Shortcut for `new HttpResource.Method("HEAD", handler)`.

**`options(HttpRequestHandler handler)`**  
⇒ *`HttpResource.Method`*  
Shortcut for `new HttpResource.Method("OPTIONS", handler)`.

**`patch(HttpRequestHandler handler)`**  
⇒ *`HttpResource.Method`*  
Shortcut for `new HttpResource.Method("PATCH", handler)`.

**`post(HttpRequestHandler handler)`**  
⇒ *`HttpResource.Method`*  
Shortcut for `new HttpResource.Method("POST", handler)`.

**`put(HttpRequestHandler handler)`**  
⇒ *`HttpResource.Method`*  
Shortcut for `new HttpResource.Method("PUT", handler)`.

**`method(String method, WebController handler)`**  
⇒ *`WebMVCResource.Method`*  
Shortcut for `new WebMVCResource.Method(method, handler)`.

**`delete(WebController handler)`**  
⇒ *`WebMVCResource.Method`*  
Shortcut for `new WebMVCResource.Method("DELETE", handler)`.

**`get(WebController handler)`**  
⇒ *`WebMVCResource.Method`*  
Shortcut for `new WebMVCResource.Method("GET", handler)`.

**`head(WebController handler)`**  
⇒ *`WebMVCResource.Method`*  
Shortcut for `new WebMVCResource.Method("HEAD", handler)`.

**`options(WebController handler)`**  
⇒ *`WebMVCResource.Method`*  
Shortcut for `new WebMVCResource.Method("OPTIONS", handler)`.

**`patch(WebController handler)`**  
⇒ *`WebMVCResource.Method`*  
Shortcut for `new WebMVCResource.Method("PATCH", handler)`.

**`post(WebController handler)`**  
⇒ *`WebMVCResource.Method`*  
Shortcut for `new WebMVCResource.Method("POST", handler)`.

**`put(WebController handler)`**  
⇒ *`WebMVCResource.Method`*  
Shortcut for `new WebMVCResource.Method("PUT", handler)`.

**`view(Function rendererFactory)`**  
⇒ *`WebView`*  
creates a View that applies the renderer provided by `rendererFactory` for
 the model to the response.

**`streamWith(Streamer streamer)`**  
⇒ *`BinaryRenderer`*  
Renderer using `streamer` to stream data to the response output stream.

**`streamFrom(InputStream inputStream)`**  
⇒ *`BinaryRenderer`*  
Renderer streaming data from `inputStream` to the response output stream.

**`writeWith(Texter texter)`**  
⇒ *`TextRenderer`*  
Renderer using `texter` to write to the response writer.

**`readFrom(Reader reader)`**  
⇒ *`TextRenderer`*  
Renderer writing data from `reader` to the response writer.

**`write(T toWrite, Function stringifier)`**  
⇒ *`TextRenderer`*  
Renderer writing `toWrite` converted into a string using `stringifier` to
 the response writer.

**`write(Object toWrite)`**  
⇒ *`TextRenderer`*  
Renderer writing `toWrite` converted into a string using `toWrite.toString`
 to the response writer.

**`filter()`**  
Dummy to avoid defining empty filters.

**`filter(HttpRequestFilter filter)`**  
⇒ *`HttpRequestFilter`*  
Shortcut to specify [`HttpRequestFilter`](#httprequestfilter) lambda style. Returns its argument.

**`filter(HttpRequestFilter[] chainedFilters)`**  
⇒ *`HttpRequestFilter`*  
Shortcut for `new HttpRequestFilterChain(chainedFilters)`.

**`chain(HttpRequestFilter[] chainedFilters)`**  
⇒ *`HttpRequestFilter`*  
Alias for `filter(HttpRequestFilter...)`.

## org.tinyj.web.mvc.filter

### HttpRequestFilter
_[(src)](src/main/java/org/tinyj/web/mvc/filter/HttpRequestFilter.java)_
_(interface)_

A HTTP request can be propagated through on ore more `HttpRequestFilter`
 instances. Each of them may prepare the response (like pre-setting headers),
 trigger side effects (like request logging) and enrich the request and
 response objects as they see fit.

**`filter(HttpServletRequest request, HttpServletResponse response, HttpRequestHandler next)`**  
An implementation can stop request propagation by not invoking `next`.

**`conclude(HttpRequestHandler eoc)`**  
⇒ *`HttpRequestHandler`*  
terminates a filter chain by adding [`HttpRequestHandler`](#httprequesthandler) `eoc` at it's end.
 Returns the resulting [`HttpRequestHandler`](#httprequesthandler).

### HttpRequestFilterChain
_[(src)](src/main/java/org/tinyj/web/mvc/filter/HttpRequestFilterChain.java)_
_implements_ [`HttpRequestFilter`](#httprequestfilter)

Composite [`HttpRequestFilter`](#httprequestfilter).

**`HttpRequestFilterChain(List chained)`** _(constructor)_  
request will be propagated through `chained` in iteration order.

## org.tinyj.web.mvc.render

### BinaryRenderer
_[(src)](src/main/java/org/tinyj/web/mvc/render/BinaryRenderer.java)_
_extends_ [`WebRendererBase`](#webrendererbase)

Renderer for HTTP responses containing binary data.

**`BinaryRenderer(Streamer streamer)`** _(constructor)_  
when rendered the response's output stream will be passed
 to `streamer` to render the response body.

**`BinaryRenderer(InputStream input)`** _(constructor)_  
when rendered everything from `input` will be copied into
 the response's output stream.

### Streamer
_[(src)](src/main/java/org/tinyj/web/mvc/render/Streamer.java)_
_(interface)_

Intended to be used in a functor style. I.e. an implementation usually carries
 data to write into an `java.io.OutputStream`.

**`stream(OutputStream output)`**  
Write data to `output`. `output` should be ready to be written to.
 By contract an implementation is required to not call `output.close()` on
 invocation.

### TextRenderer
_[(src)](src/main/java/org/tinyj/web/mvc/render/TextRenderer.java)_
_extends_ [`WebRendererBase`](#webrendererbase)

Renderer for HTTP responses containing text. By default `UTF-8` is used as
 character encoding.

**`TextRenderer(Texter texter)`** _(constructor)_  
when rendered the response's writer will be passed to `texter` to render
 the response body.

**`TextRenderer(Reader reader)`** _(constructor)_  
when rendered everything from `reader` will be copied to
 the response's writer.

### Texter
_[(src)](src/main/java/org/tinyj/web/mvc/render/Texter.java)_
_(interface)_

Intended to be used in a functor style. I.e. an implementation usually
 carries data to write into a `java.io.Writer`.

**`writeTo(Writer writer)`**  
Write data to `writer`. `writer` should be ready to be written to.
 By contract an implementation is required to not call `writer.close()`
 on invocation.

### WebRendererBase
_[(src)](src/main/java/org/tinyj/web/mvc/render/WebRendererBase.java)_
_(abstract)_  
_implements_ [`WebRenderer`](#webrenderer)

Abstract [`WebRenderer`](#webrenderer) facilitating the definition and rendering
 of HTTP headers.

**`withStatus(int status)`**  
⇒ *`WebRendererBase`*  
Response will be rendered with status code `status`.

**`withContentType(String contentType)`**  
⇒ *`WebRendererBase`*  
Response will be rendered with the _Content-Type_ header set to
 `contentType`. This overrides _Content-Type_ headers set with `withHeader`

**`withEncoding(String encoding)`**  
⇒ *`WebRendererBase`*  
When rendered `encoding` will be added to the _Content-Type_ header. If
 the response is rendered using the respond's writer `encoding` is used as
 character encoding.

**`withHeader(String name, String[] values)`**  
⇒ *`WebRendererBase`*  
response will be rendered with a `name`-header line for each passed value.

**`renderHeader(HttpServletResponse response)`**  
render response headers (including status line). This method is called
 before `renderBody`.

**`renderBody(HttpServletResponse response)`**  
Render response body. This method is called after `renderHeader`.

## org.tinyj.web.mvc.resource

### HttpResource
_[(src)](src/main/java/org/tinyj/web/mvc/resource/HttpResource.java)_
_implements_ [`HttpRequestHandler`](#httprequesthandler)

Dispatches a request to a HTTP resource to a set of method handlers.

 `HttpResource` comes with three default handlers:

 - OPTIONS requests are answered with the _Allow_ header set to the list of
 supported methods.
 - if a GET handler is registered, HEAD requests are dispatched to the GET
 handler but the response body is discarded.
 - if fallback (*) is registered, requests for unregistered requests are
 answered with "405 Method not allowed!". The _Allow_ header set to the list
 of supported methods.

**`HttpResource(Method[] handlers)`** _(constructor)_  
Create new `HttpResource` dispatching requests to `handlers`. A handler
 for `*` is removed from the list an registered as fallback handler.

 If `handlers` contains multiple handlers for the same method the later wins.

**`supportedMethods()`**  
⇒ *`Set`* _(the supported Methods)_  
returns the list of supported methods for the default implementations of the
 OPTIONS handler and the fallback handler.

**`options(HttpServletRequest request, HttpServletResponse response)`**  
default OPTIONS method handler

**`head(HttpServletRequest request, HttpServletResponse response)`**  
default HEAD method handler

**`methodNotAllowed(HttpServletRequest request, HttpServletResponse response)`**  
default method handler fallback

### WebMVCResource\<X>
_[(src)](src/main/java/org/tinyj/web/mvc/resource/WebMVCResource.java)_
_implements_ [`WebController`](#webcontrollerx)

Dispatches a request to a HTTP resource to a set of method handlers.

**`WebMVCResource(Method[] handlers)`** _(constructor)_  
Create new `WebMVCResource` dispatching requests to `handlers`. If a handler
 for `*` is passed it's used as fallback handler. The default fallback is to
 return `null` as result model.

 If `handlers` contains multiple handlers for the same method the later wins.

## org.tinyj.web.mvc.route

### HttpRequestDispatcher
_[(src)](src/main/java/org/tinyj/web/mvc/route/HttpRequestDispatcher.java)_
_implements_ [`HttpRequestHandler`](#httprequesthandler)

Dispatches `HttpServletRequest` matching the request's pathInfo against a set
 of routes.

### WebMVCRequestDispatcher\<X>
_[(src)](src/main/java/org/tinyj/web/mvc/route/WebMVCRequestDispatcher.java)_
_implements_ [`WebController`](#webcontrollerx)

Dispatches `HttpServletRequest` matching the request's pathInfo against a set
 of routes.

