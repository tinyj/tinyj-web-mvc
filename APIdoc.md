## org.tinyj.web.mvc

### DSL
_[(src)](src/main/java/org/tinyj/web/mvc/DSL.java)_ |
_(interface)_

Helper methods to define a domain specific language to simplify creating
 Request filter chains, routing tables and handlers.

**`mvc(WebView view, WebController controller)`**  
⇒ *`HttpRequestHandler`*  
Shortcut for `new MVCBridge(view, controller)`.

**`mvc(WebView view, Method[] methods)`**  
⇒ *`HttpRequestHandler`*  
Shortcut for `mvc(view, controller(methods)).

**`resource(Method[] handlers)`**  
⇒ *`HttpResource`*  
Shortcut for `new HttpResource(handlers)`, see [`HttpResource`](#httpresource)

**`controller(Method[] methods)`**  
⇒ *`WebMVCResource`*  
Shortcut for `new WebMVCResource(methods)`, see [`WebMVCResource`](#webmvcresourcex)

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

**`dispatch(Route[] routes)`**  
⇒ *`HttpRequestDispatcher`*  
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

**`mvc(String target, WebView view, Method[] methods)`**  
⇒ *`HttpRequestDispatcher.Route`*  
Shortcut for `route(target, mvc(view, controller(methods)))`.

**`dispatch(String target, Route[] routes)`**  
⇒ *`HttpRequestDispatcher.Route`*  
Shortcut for `route(target, dispatch(routes))`.

**`dispatch(Route[] routes)`**  
⇒ *`WebMVCRequestDispatcher`*  
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

**`streamUsing(Streamer streamer)`**  
⇒ *`HttpRenderer`*  
Renderer using `streamer` to stream data to the response output stream.

**`streamFrom(InputStream inputStream)`**  
⇒ *`HttpRenderer`*  
Renderer streaming data from `inputStream` to the response output stream.

**`writeUsing(Texter texter)`**  
⇒ *`HttpRenderer`*  
Renderer using `texter` to write to the response writer.

**`writeFrom(Reader reader)`**  
⇒ *`HttpRenderer`*  
Renderer writing text from `reader` to the response writer.

**`writeFrom(T toWrite, Function stringifier)`**  
⇒ *`HttpRenderer`*  
Renderer writing `toWrite` converted into a string using `stringifier` to
 the response writer.

**`writeFrom(Object toWrite)`**  
⇒ *`HttpRenderer`*  
Renderer writing `toWrite` converted into a string using `toWrite.toString`
 to the response writer.

### HttpRequestFilter
_[(src)](src/main/java/org/tinyj/web/mvc/HttpRequestFilter.java)_ |
_(interface)_

A HTTP request can be propagated through on ore more `HttpRequestFilter`
 instances. Each of them may prepare the response (like pre-setting headers),
 trigger side effects (like request logging) and enrich the request and
 response objects as they see fit.

**`filter(HttpServletRequest request, HttpServletResponse response, HttpRequestHandler next)`**  
An implementation can stop request propagation by not invoking `next`.

**`terminate(HttpRequestHandler eoc)`**  
⇒ *`HttpRequestHandler`*  
terminates the filter chain with `eoc`. Returns the resulting
 [`HttpRequestHandler`](#httprequesthandler).

### HttpRequestFilterChain
_[(src)](src/main/java/org/tinyj/web/mvc/HttpRequestFilterChain.java)_  
_implements_ [`HttpRequestFilter`](#httprequestfilter)

Composite [`HttpRequestFilter`](#httprequestfilter).

**`HttpRequestFilterChain(List chained)`** _(constructor)_  
request will be propagated through `chained` in iteration order.

### HttpRequestFilterChain.Walker
_[(src)](src/main/java/org/tinyj/web/mvc/HttpRequestFilterChain.java)_  
_implements_ [`HttpRequestHandler`](#httprequesthandler)

Helper class to iterate over a list of filters. Invokes filters in
 iteration order inserting itself as `next`.

### HttpRequestHandler
_[(src)](src/main/java/org/tinyj/web/mvc/HttpRequestHandler.java)_ |
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
_[(src)](src/main/java/org/tinyj/web/mvc/WebController.java)_ |
_(interface)_ |
X: Type of the result model.

A conceptual variation of the [`HttpRequestHandler`](#httprequesthandler) interface. Instead of
 directly rendering into a passed `HttpServletResponse` output parameter a model
 describing the result is returned, leaving the final rendering to another party.

**`handle(HttpServletRequest request)`**  
⇒ *`X`* _(the result model)_  


### WebResponseView\<T>
_[(src)](src/main/java/org/tinyj/web/mvc/WebResponseView.java)_ |
_(abstract)_  
_implements_ [`WebView`](#webviewx)

Base class for [`WebView`](#webviewx) implementations rendering [`WebResponse`](#webresponset).

**`render(WebResponse model, HttpServletRequest request, HttpServletResponse response)`**  
Calls `renderHeader()` and `renderBody()`.

**`renderHeader(WebResponse model, HttpServletRequest request, HttpServletResponse response)`**  
Default implementation sets the response status and calls
 `response.setHeader(...)` for each header passed to `model`.

 After that `response.setContentType(...)` and `response.setEncoding(...)` are
 called with the values passed to `model`.

**`renderBody(WebResponse model, HttpServletRequest request, HttpServletResponse response)`**  
Render the response body. This method is called after `renderHeader(...)`.

### WebView\<X>
_[(src)](src/main/java/org/tinyj/web/mvc/WebView.java)_ |
_(interface)_ |
X: model type.

A `WebView` is responsible for rendering the result model of an [`WebController`](#webcontrollerx)
 into a HTTP response.

**`render(X model, HttpServletRequest request, HttpServletResponse response)`**  
Render `model` into `response`.

 By passing `response` all claims onto `response` are yielded. I.e. after
 returning `response` may be in any state, it may or may not be committed,
 data may or may not been send.

 Even though it's not a strict requirement from this interface, most
 implementations will assume that prior to invocation `response` is uncommitted.

## org.tinyj.web.mvc.render

### BinaryRenderer
_[(src)](src/main/java/org/tinyj/web/mvc/render/BinaryRenderer.java)_  
_extends_ [`HttpRenderer`](#httprenderer)

Renderer for HTTP responses containing binary data.

**`BinaryRenderer(Streamer streamer)`** _(constructor)_  
when rendered the response's output stream will be passed
 to `streamer` to render the response body.

**`renderBody(HttpServletRequest request, HttpServletResponse response)`**  
Use the [`Streamer`](#streamer) passed to the constructor to stream data to
 `response`s output stream.

### HttpRenderer
_[(src)](src/main/java/org/tinyj/web/mvc/render/HttpRenderer.java)_ |
_(abstract)_  
_implements_ [`HttpRequestHandler`](#httprequesthandler)

Abstract class facilitating the definition and rendering of HTTP headers.

**`withStatus(int status)`**  
⇒ *`HttpRenderer`*  
Response will be rendered with status code `status`.

**`withContentType(String contentType)`**  
⇒ *`HttpRenderer`*  
Response will be rendered with the _Content-Type_ header set to
 `contentType`. This overrides _Content-Type_ headers set with `withHeader`

**`withEncoding(String encoding)`**  
⇒ *`HttpRenderer`*  
When rendered `encoding` will be added to the _Content-Type_ header. If
 the response is rendered using the respond's writer `encoding` is used as
 character encoding.

**`withHeader(String name, String[] values)`**  
⇒ *`HttpRenderer`*  
response will be rendered with a `name`-header line for each passed value.

**`renderHeader(HttpServletRequest request, HttpServletResponse response)`**  
render response headers (including status line). This method is called
 before `renderBody`.

**`renderBody(HttpServletRequest request, HttpServletResponse response)`**  
Render response body. This method is called after `renderHeader`.

### Streamer
_[(src)](src/main/java/org/tinyj/web/mvc/render/Streamer.java)_ |
_(interface)_

Intended to be used in a functor style. I.e. an implementation usually carries
 data to write into an `java.io.OutputStream`.

**`stream(OutputStream output)`**  
Write data to `output`. `output` should be ready to be written to.
 By contract an implementation is required to not call `output.close()` on
 invocation.

**`streamFrom(InputStream input)`**  
⇒ *`Streamer`*  
Creates a `Streamer` that, when invoked, streams all data from `input`.

### TextRenderer
_[(src)](src/main/java/org/tinyj/web/mvc/render/TextRenderer.java)_  
_extends_ [`HttpRenderer`](#httprenderer)

Renderer for HTTP responses containing text. By default `UTF-8` is used as
 character encoding.

**`TextRenderer(Texter texter)`** _(constructor)_  
when rendered the response's writer will be passed to `texter` to render
 the response body.

**`renderBody(HttpServletRequest request, HttpServletResponse response)`**  
Use the [`Texter`](#texter) passed to the constructor to write to `response`s
 writer.

### Texter
_[(src)](src/main/java/org/tinyj/web/mvc/render/Texter.java)_ |
_(interface)_

Intended to be used in a functor style. I.e. an implementation usually
 carries data to write into a `java.io.Writer`.

**`writeTo(Writer writer)`**  
Write data to `writer`. `writer` should be ready to be written to.
 By contract an implementation is required to not call `writer.close()`
 on invocation.

**`textFrom(T toWrite)`**  
⇒ *`Texter`*  
Creates a `Texter` that writes `toWrite.toString()` on invocation.

**`textFrom(T toWrite, Function stringifier)`**  
⇒ *`Texter`*  
Creates a `Texter` that, when invoked, writes `toWrite` converted to
 string using `stringifier`.

**`textFrom(Reader reader)`**  
⇒ *`Texter`*  
Creates a `Texter` that writes all text from `reader` on invocation.

## org.tinyj.web.mvc.resource

### HttpResource
_[(src)](src/main/java/org/tinyj/web/mvc/resource/HttpResource.java)_  
_implements_ [`HttpRequestHandler`](#httprequesthandler)

Dispatches a request to a HTTP resource to a set of method handlers.

**`HttpResource(Method[] handlers)`** _(constructor)_  
Create new `HttpResource` dispatching requests to `handlers`. A handler
 for `*` is removed from the list an registered as fallback handler.

 If `handlers` contains multiple handlers for the same method the later wins.

**`setMethods(Method[] handlers)`**  
Register method handlers

**`options(HttpServletRequest request, HttpServletResponse response)`**  
Default OPTIONS method handler

**`methodNotAllowed(HttpServletRequest request, HttpServletResponse response)`**  
Default method handler fallback, throws [`MethodNotAllowedException`](#methodnotallowedexception)

**`supportedMethods()`**  
⇒ *`Set<String>`* _(the supported Methods)_
Returns the list of supported methods.

### MethodNotAllowedException
_[(src)](src/main/java/org/tinyj/web/mvc/resource/MethodNotAllowedException.java)_  
_extends_ (#RuntimeException)

Indicates that a HTTP request could not be answered because the request
 method is not allowed.

**`getAllowed()`**  
⇒ *`String`*  
List of supported methods supported by the HTTP resource.

### WebMVCResource\<X>
_[(src)](src/main/java/org/tinyj/web/mvc/resource/WebMVCResource.java)_  
_implements_ [`WebController`](#webcontrollerx)

Dispatches a request to a HTTP resource to a set of method handlers.

**`WebMVCResource(Method[] handlers)`** _(constructor)_  
Create new `WebMVCResource` dispatching requests to `handlers`. If a handler
 for `*` is passed it's used as fallback handler. The default fallback is to
 throw a [`MethodNotAllowedException`](#methodnotallowedexception).

 If `handlers` contains multiple handlers for the same method the later wins.

**`setMethods(Method[] handlers)`**  
register method handlers

**`methodNotAllowed(HttpServletRequest request)`**  
⇒ *`X`*  
Default method handler fallback, throws [`MethodNotAllowedException`](#methodnotallowedexception)

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

