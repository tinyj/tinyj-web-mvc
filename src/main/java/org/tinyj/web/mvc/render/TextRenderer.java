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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Renderer for HTTP responses containing text. By default `UTF-8` is used as
 * character encoding.
 */
public class TextRenderer
    extends HttpRenderer {

  protected final Texter texter;

  /**
   * when rendered the response's writer will be passed to `texter` to render
   * the response body.
   */
  public TextRenderer(Texter texter) {
    withContentType("text/plain");
    withEncoding("UTF-8");
    this.texter = texter;
  }

  /**
   * Use the (#Texter) passed to the constructor to write to `response`s
   * writer.
   */
  @Override
  public void renderBody(HttpServletRequest request, HttpServletResponse response) throws Exception {
    texter.writeTo(response.getWriter());
  }
}
