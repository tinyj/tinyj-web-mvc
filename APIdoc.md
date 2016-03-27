## org.tinyj.web.mvc

### DSL
_[(src)](src/main/java/org/tinyj/web/mvc/DSL.java)_ |
_(interface)_

Helper methods to define a domain specific language to simplify creating
 Request filter chains, routing tables and handlers.

**`mvc(WebView<? super X> view, WebController<? extends X> controller)`**  
⇒ *`HttpRequestHandler`*  
Shortcut for `new MVCBridge(view, controller)`.

**`mvc(WebView<? super X> view, WebResource.MethodHandler<? extends X>[] methodHandlers)`**  
⇒ *`HttpRequestHandler`*  
Shortcut for `mvc(view, controller(methodHandlers)).

**`resource(HttpResource.MethodHandler[] methodHandlers)`**  
⇒ *`HttpResource`*  
Shortcut for `new HttpResource(methodHandlers)`, see [`HttpResource`](#httpresource)

**`controller(WebResource.MethodHandler<? extends X>[] methodHandlers)`**  
⇒ *`WebResource<X>`*  
Shortcut for `new WebController(methodHandlers)`, see [`WebController`](#webcontrollerx)

**`method(String method, HttpRequestHandler handler)`**  
⇒ *`HttpResource.MethodHandler`*  
Shortcut for `new HttpResource.MethodHandler(method, handler)`.

**`delete(HttpRequestHandler handler)`**  
⇒ *`HttpResource.MethodHandler`*  
Shortcut for `new HttpResource.MethodHandler("DELETE", handler)`.

**`get(HttpRequestHandler handler)`**  
⇒ *`HttpResource.MethodHandler`*  
Shortcut for `new HttpResource.MethodHandler("GET", handler)`.

**`head(HttpRequestHandler handler)`**  
⇒ *`HttpResource.MethodHandler`*  
Shortcut for `new HttpResource.MethodHandler("HEAD", handler)`.

**`options(HttpRequestHandler handler)`**  
⇒ *`HttpResource.MethodHandler`*  
Shortcut for `new HttpResource.MethodHandler("OPTIONS", handler)`.

**`patch(HttpRequestHandler handler)`**  
⇒ *`HttpResource.MethodHandler`*  
Shortcut for `new HttpResource.MethodHandler("PATCH", handler)`.

**`post(HttpRequestHandler handler)`**  
⇒ *`HttpResource.MethodHandler`*  
Shortcut for `new HttpResource.MethodHandler("POST", handler)`.

**`put(HttpRequestHandler handler)`**  
⇒ *`HttpResource.MethodHandler`*  
Shortcut for `new HttpResource.MethodHandler("PUT", handler)`.

**`method(String method, WebController<X> handler)`**  
⇒ *`WebResource.MethodHandler<X>`*  
Shortcut for `new WebController.MethodHandler(method, handler)`.

**`delete(WebController<X> handler)`**  
⇒ *`WebResource.MethodHandler<X>`*  
Shortcut for `new WebController.MethodHandler("DELETE", handler)`.

**`get(WebController<X> handler)`**  
⇒ *`WebResource.MethodHandler<X>`*  
Shortcut for `new WebController.MethodHandler("GET", handler)`.

**`head(WebController<X> handler)`**  
⇒ *`WebResource.MethodHandler<X>`*  
Shortcut for `new WebController.MethodHandler("HEAD", handler)`.

**`options(WebController<X> handler)`**  
⇒ *`WebResource.MethodHandler<X>`*  
Shortcut for `new WebController.MethodHandler("OPTIONS", handler)`.

**`patch(WebController<X> handler)`**  
⇒ *`WebResource.MethodHandler<X>`*  
Shortcut for `new WebController.MethodHandler("PATCH", handler)`.

**`post(WebController<X> handler)`**  
⇒ *`WebResource.MethodHandler<X>`*  
Shortcut for `new WebController.MethodHandler("POST", handler)`.

**`put(WebController<X> handler)`**  
⇒ *`WebResource.MethodHandler<X>`*  
Shortcut for `new WebController.MethodHandler("PUT", handler)`.

**`dispatch(HttpRequestDispatcher.Route[] routes)`**  
⇒ *`HttpRequestDispatcher`*  
Dispatch requests based on their path info. See [`HttpRequestDispatcher`](#httprequestdispatcher).

**`route(String target, HttpRequestHandler handler)`**  
⇒ *`HttpRequestDispatcher.Route`*  
Creates a dispatcher routing entry.

**`resource(String target, HttpResource.MethodHandler[] methodHandlers)`**  
⇒ *`HttpRequestDispatcher.Route`*  
Shortcut for `route(target, resource(methodHandlers))`.

**`mvc(String target, WebView<? super X> view, WebController<X> controller)`**  
⇒ *`HttpRequestDispatcher.Route`*  
Shortcut for `route(target, mvc(view, controller))`.

**`mvc(String target, WebView<? super X> view, WebResource.MethodHandler<? extends X>[] methodHandlers)`**  
⇒ *`HttpRequestDispatcher.Route`*  
Shortcut for `route(target, mvc(view, controller(methodHandlers)))`.

**`dispatch(String target, HttpRequestDispatcher.Route[] routes)`**  
⇒ *`HttpRequestDispatcher.Route`*  
Shortcut for `route(target, dispatch(routes))`.

**`dispatch(WebMVCRequestDispatcher.Route<? extends X>[] routes)`**  
⇒ *`WebMVCRequestDispatcher<X>`*  
Dispatch requests based on their path info. See [`WebMVCRequestDispatcher`](#webmvcrequestdispatcherx)

**`route(String path, WebController<X> controller)`**  
⇒ *`WebMVCRequestDispatcher.Route<X>`*  
Creates a dispatcher routing entry

**`controller(String path, WebResource.MethodHandler<? extends X>[] methodHandlers)`**  
⇒ *`WebMVCRequestDispatcher.Route<X>`*  
Shortcut for `route(target, controller(methodHandlers))`.

**`dispatch(String path, WebMVCRequestDispatcher.Route<? extends X>[] routes)`**  
⇒ *`WebMVCRequestDispatcher.Route<X>`*  
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

**`writeFrom(T toWrite, Function<? super T, String> stringifier)`**  
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

**`HttpRequestFilterChain(List<HttpRequestFilter> chained)`** _(constructor)_  
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

### HttpResource
_[(src)](src/main/java/org/tinyj/web/mvc/HttpResource.java)_  
_implements_ [`HttpRequestHandler`](#httprequesthandler)

Dispatches a request to a HTTP resource to a set of method handlers.

**`HttpResource(HttpResource.MethodHandler[] handlers)`** _(constructor)_  
Create new `HttpResource` dispatching requests to `handlers`. A handler
 for `*` is used as fallback handler.

 If `handlers` contains multiple handlers for the same method the last one is used.

**`setMethods(HttpResource.MethodHandler[] handlers)`**  
Register method handlers.

**`methodNotAllowed(HttpServletRequest request, HttpServletResponse response)`**  
Default method handler fallback, throws [`MethodNotAllowedException`](#methodnotallowedexception).

**`supportedMethods()`**  
⇒ *`Set<String>`* _(the supported Methods)_  
Returns the list of supported methods.

### MethodNotAllowedException
_[(src)](src/main/java/org/tinyj/web/mvc/MethodNotAllowedException.java)_  
_extends_ (#RuntimeException)

Indicates that a HTTP request could not be answered because the request
 method is not allowed.

**`getAllowed()`**  
⇒ *`String[]`*  
List of supported methods supported by the HTTP resource.

### WebController\<X>
_[(src)](src/main/java/org/tinyj/web/mvc/WebController.java)_ |
_(interface)_ |
X: Type of the result model.

A conceptual variation of the [`HttpRequestHandler`](#httprequesthandler) interface. Instead of
 directly rendering into a passed `HttpServletResponse` output parameter a model
 describing the result is returned, leaving the final rendering to another party.

**`handle(HttpServletRequest request)`**  
⇒ *`X`* _(the result model)_  


### WebResource\<X>
_[(src)](src/main/java/org/tinyj/web/mvc/WebResource.java)_  
_implements_ [`WebController`](#webcontrollerx)

Dispatches a request to a HTTP resource to a set of method handlers.

**`WebResource(WebResource.MethodHandler<? extends X>[] handlers)`** _(constructor)_  
Create new `WebResource` dispatching requests to `handlers`. A handler
 for `*` is used as fallback handler.

 If `handlers` contains multiple handlers for the same method the last one is used.

**`setMethods(WebResource.MethodHandler<? extends X>[] handlers)`**  
Register method handlers.

**`methodNotAllowed(HttpServletRequest request)`**  
⇒ *`X`*  
Default method handler fallback, throws [`MethodNotAllowedException`](#methodnotallowedexception).

**`supportedMethods()`**  
⇒ *`Set<String>`* _(the supported Methods)_  
Returns the list of supported methods.

### WebResponseView\<T>
_[(src)](src/main/java/org/tinyj/web/mvc/WebResponseView.java)_ |
_(abstract)_  
_implements_ [`WebView`](#webviewx)

Base class for [`WebView`](#webviewx) implementations rendering [`WebResponse`](#webresponset).

**`render(WebResponse<T> model, HttpServletRequest request, HttpServletResponse response)`**  
Calls `renderHeader()` and `renderBody()`.

**`renderHeader(WebResponse<T> model, HttpServletRequest request, HttpServletResponse response)`**  
Default implementation sets the response status and calls
 `response.setHeader(...)` for each header passed to `model`.

 After that `response.setContentType(...)` and `response.setEncoding(...)` are
 called with the values passed to `model`.

**`renderBody(WebResponse<T> model, HttpServletRequest request, HttpServletResponse response)`**  
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

**`textFrom(T toWrite, Function<? super T, String> stringifier)`**  
⇒ *`Texter`*  
Creates a `Texter` that, when invoked, writes `toWrite` converted to
 string using `stringifier`.

**`textFrom(Reader reader)`**  
⇒ *`Texter`*  
Creates a `Texter` that writes all text from `reader` on invocation.

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

