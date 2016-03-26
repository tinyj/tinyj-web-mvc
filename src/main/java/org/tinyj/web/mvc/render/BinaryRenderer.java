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

/** Renderer for HTTP responses containing binary data. */
public class BinaryRenderer
    extends HttpRenderer {

  protected final Streamer streamer;

  /**
   * when rendered the response's output stream will be passed
   * to `streamer` to render the response body.
   */
  public BinaryRenderer(Streamer streamer) {
    withContentType("application/octet-stream");
    this.streamer = streamer;
  }

  /**
   * Use the (#Streamer) passed to the constructor to stream data to
   * `response`s output stream.
   */
  @Override
  public void renderBody(HttpServletRequest request, HttpServletResponse response) throws Exception {
    streamer.stream(response.getOutputStream());
  }

}
