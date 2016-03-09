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
package org.example.key_value.status;

import org.tinyj.web.mvc.WebView;

import javax.servlet.http.HttpServletResponse;

public class StatusView implements WebView<Status> {

  @Override
  public void render(Status model, HttpServletResponse response) throws Exception {
    switch (model.deploymentStatus) {
      case BOOTING:
        response.setStatus(503, "Service is still booting up, please try again later.");
        break;
      case RUNNING:
        response.setStatus(200);
        break;
      case PAUSED:
        response.setStatus(503, "Service temporarily unavailable.");
        break;
      case SHUTTING_DOWN:
        response.setStatus(410, "Service is shutting down.");
        break;
    }
    response.setContentType("text/plain");
    response.setCharacterEncoding("UTF-8");
    response.getWriter()
        .append("entry count:").append(Integer.toString(model.entryCount)).append("\r\n")
        .append("deploy status:").append(model.deploymentStatus.name()).append("\r\n");
  }
}