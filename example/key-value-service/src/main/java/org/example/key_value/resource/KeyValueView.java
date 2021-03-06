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
package org.example.key_value.resource;

import org.tinyj.web.mvc.WebResponse;
import org.tinyj.web.mvc.WebResponseView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Collection;

public class KeyValueView extends WebResponseView {

  @Override
  protected void renderBody(WebResponse webResponse, HttpServletRequest request, HttpServletResponse response) throws Exception {
    final Object model = webResponse.getModel();
    final PrintWriter writer = response.getWriter();
    if (model instanceof Collection<?>) {
      if (((Collection) model).isEmpty()) {
        response.setStatus(204);
      }
      for (Object entry : (Collection<?>) model) {
        writer.append(entry.toString()).append("\r\n");
      }
    } else if (model != null) {
      final String body = model.toString();
      if (body.isEmpty()) {
        response.setStatus(204);
      }
      writer.write(body);
    }
  }
}