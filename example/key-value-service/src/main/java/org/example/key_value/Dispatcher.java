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
package org.example.key_value;

import org.example.key_value.resource.StoreEndpoint;
import org.example.key_value.status.StatusController;
import org.example.key_value.status.StatusView;
import org.tinyj.web.mvc.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;

import static org.tinyj.web.mvc.dsl.DSL.*;

public class Dispatcher implements HttpRequestHandler {

  private HttpRequestHandler dispatch;

  public Dispatcher(StatusController statusController, StoreEndpoint storeEndpoint) {
    dispatch = filter(this::exceptionHandler)
        .conclude(dispatch(
            mvc("/status", new StatusView(), statusController),
            vc("/store/*", storeEndpoint)));
  }

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    try {
      dispatch.handle(request, response);
    } catch (Exception e) {
      throw new ServletException(e);
    }
  }

  public void exceptionHandler(HttpServletRequest request, HttpServletResponse response, HttpRequestHandler next) throws Exception {
    try {
      next.handle(request, response);
    } catch (IllegalArgumentException e) {
      if (!response.isCommitted()) {
        sendErrorWithBody(response, 406, e.getMessage());
      }
    } catch (NoSuchElementException e) {
      if (!response.isCommitted()) {
        sendErrorWithBody(response, 404, e.getMessage());
      }
    } catch (UnsupportedOperationException e) {
      if (!response.isCommitted()) {
        sendErrorWithBody(response, 405, e.getMessage());
      }
    } catch (Exception e) {
      if (!response.isCommitted()) {
        sendErrorWithBody(response, 500, "Internal server error.");
      }
    }
  }

  private void sendErrorWithBody(HttpServletResponse response, int status, String message) throws IOException {
    response.setStatus(status);
    response.getWriter().println(message);
    response.flushBuffer();
  }

}