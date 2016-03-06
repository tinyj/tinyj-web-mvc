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
package org.tinyj.web.mvc.render;

import org.tinyj.web.mvc.WebRenderer;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract (#HttpResponseRenderer) facilitating the definition and rendering
 * of HTTP headers.
 */
public abstract class WebRendererBase implements WebRenderer {

  protected int status;
  protected String contentType;
  protected String encoding;
  protected Map<String, String[]> headers = new HashMap<>();

  public WebRendererBase() {
    this.status = 200;
  }

  /** Response will be rendered with status code `status`. */
  public WebRendererBase withStatus(int status) {
    this.status = status;
    return this;
  }

  /**
   * Response will be rendered with the _Content-Type_ header set to
   * `contentType`. This overrides _Content-Type_ headers set with `withHeader`
   */
  public WebRendererBase withContentType(String contentType) {
    this.contentType = contentType;
    return this;
  }

  /**
   * When rendered `encoding` will be added to the _Content-Type_ header. If
   * the response is rendered using the respond's writer `encoding` is used as
   * character encoding.
   */
  public WebRendererBase withEncoding(String encoding) {
    this.encoding = encoding;
    return this;
  }

  /** response will be rendered with a `name`-header line for each passed value. */
  public WebRendererBase withHeader(String name, String... values) {
    this.headers.put(name, values);
    return this;
  }

  @Override
  public void render(HttpServletResponse response) throws Exception {
    renderHeader(response);
    renderBody(response);
  }

  /**
   * render response headers (including status line). This method is called
   * before `renderBody`.
   */
  protected void renderHeader(HttpServletResponse response) {
    response.setStatus(status);
    for (Map.Entry<String, String[]> header : headers.entrySet()) {
      String name = header.getKey();
      for (String value : header.getValue()) {
        response.addHeader(name, value);
      }
    }
    if (contentType != null) {
      response.setContentType(contentType);
    }
    if (encoding != null) {
      response.setCharacterEncoding(encoding);
    }
  }

  /** Render response body. This method is called after `renderHeader`. */
  protected abstract void renderBody(HttpServletResponse request) throws Exception;
}
