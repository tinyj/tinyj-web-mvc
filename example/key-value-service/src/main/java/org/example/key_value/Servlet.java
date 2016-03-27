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

import org.example.key_value.resource.KeyValueView;
import org.example.key_value.status.StatusView;
import org.tinyj.web.mvc.HttpRequestHandler;
import org.tinyj.web.mvc.HttpServletAdapter;
import org.tinyj.web.mvc.MethodNotAllowedException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.tinyj.web.mvc.DSL.*;

@WebServlet("/service/*")
public class Servlet extends HttpServletAdapter {

  private final static Module module = new Module();

  public Servlet() {
    super((Servlet self) ->
              filter(
                  self::exceptionHandler,
                  new HeadRequestFilter()
              ).terminate(
                  dispatch(
                      mvc("/status", new StatusView(), module.statusController()),
                      mvc("/store/*", new KeyValueView(), dispatch(
                          route("", module.storeController()),
                          route("/*", module.keyValueController())
                      ))
                  )));
  }

  public void exceptionHandler(HttpServletRequest request, HttpServletResponse response, HttpRequestHandler next) throws IOException {
    try {
      next.handle(request, response);
    } catch (IllegalArgumentException e) {
      if (!response.isCommitted()) {
        sendErrorWithBody(response, 400, e.getMessage());
      }
    } catch (NoSuchElementException e) {
      if (!response.isCommitted()) {
        sendErrorWithBody(response, 404, e.getMessage());
      }
    } catch (MethodNotAllowedException e) {
      if (!response.isCommitted()) {
        try {
          response.reset();
          response.setHeader("Allow", String.join(",", Stream.concat(
              Stream.of(e.getAllowed()), Stream.of("OPTIONS")).collect(toSet())));
          if (request.getMethod().toLowerCase().equals("options")) {
            response.setStatus(200);
          } else
            response.setStatus(405);
        } catch (Exception ignored) {
          sendErrorWithBody(response, 405, e.getMessage());
        }
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
