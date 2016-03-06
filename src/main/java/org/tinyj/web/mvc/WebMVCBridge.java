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

/**
 * Bridges (#WebController) to (#HttpRequestHandler).
 *
 * @param <X> Type of the result model.
 */
public class WebMVCBridge<X> implements HttpRequestHandler {

  protected final WebController<? extends X> handler;
  protected final WebView<? super X> view;

  /**
   * Translate `handler` into a (#HttpRequestHandler) by using `view` to render
   * the result model into a `HttpServletResponse`.
   */
  public WebMVCBridge(WebView<? super X> view, WebController<? extends X> handler) {
    this.handler = handler;
    this.view = view;
  }

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
    view.render(handler.handle(request), response);
  }
}
