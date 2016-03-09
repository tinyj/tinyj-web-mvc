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

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public abstract class WebResponseViewBase<T> implements WebView<WebResponse<T>> {

  @Override
  public void render(WebResponse<T> model, HttpServletResponse response) throws Exception {
    renderHeader(model, response);
    renderBody(model, response);
  }

  protected void renderHeader(WebResponse<T> model, HttpServletResponse response) throws Exception {
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

  protected abstract void renderBody(WebResponse<T> model, HttpServletResponse response) throws Exception;
}
