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
import java.util.Map;

/** Base class for (#WebView) implementations rendering (#WebResponse). */
public abstract class WebResponseView<T> implements WebView<WebResponse<T>> {

  /** Calls `renderHeader()` and `renderBody()`. */
  @Override
  public void render(WebResponse<T> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
    renderHeader(model, request, response);
    renderBody(model, request, response);
  }

  /**
   * Default implementation sets the response status and calls
   * `response.setHeader(...)` for each header passed to `model`.
   *
   * After that `response.setContentType(...)` and `response.setEncoding(...)` are
   * called with the values passed to `model`.
   */
  protected void renderHeader(WebResponse<T> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
    response.setStatus(model.getStatus());
    for (Map.Entry<String, String[]> header : model.getHeaders().entrySet()) {
      String name = header.getKey();
      for (String value : header.getValue()) {
        response.addHeader(name, value);
      }
    }
    if (model.getContentType() != null) {
      response.setContentType(model.getContentType());
    }
    if (model.getEncoding() != null) {
      response.setCharacterEncoding(model.getEncoding());
    }
  }

  /** Render the response body. This method is called after `renderHeader(...)`. */
  protected abstract void renderBody(WebResponse<T> model, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
