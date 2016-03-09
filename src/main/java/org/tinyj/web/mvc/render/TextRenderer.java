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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * Renderer for HTTP responses containing text. By default `UTF-8` is used as
 * character encoding.
 */
public class TextRenderer
    extends WebRendererBase {

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
   * when rendered everything from `reader` will be copied to
   * the response's writer.
   */
  public TextRenderer(Reader reader) {
    this(reader, 4096);
  }

  public TextRenderer(Reader reader, int bufferSize) {
    this(writer -> copy(reader, writer, new char[bufferSize]));
  }

  @Override
  public void renderBody(HttpServletResponse response) throws Exception {
    texter.writeTo(response.getWriter());
  }

  protected static void copy(Reader reader, Writer writer, char[] buffer) throws IOException {
    int read;
    while ((read = reader.read(buffer)) >= 0) {
      writer.write(buffer, 0, read);
    }
  }
}
