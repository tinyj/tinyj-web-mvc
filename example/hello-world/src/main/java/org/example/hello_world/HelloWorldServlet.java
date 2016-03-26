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
package org.example.hello_world;

import org.tinyj.web.mvc.WebView;
import org.tinyj.web.mvc.resource.WebMVCResource;
import org.tinyj.web.mvc.route.HttpRequestDispatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static org.tinyj.web.mvc.DSL.*;
import static org.tinyj.web.mvc.render.Texter.textFrom;

public class HelloWorldServlet extends HttpRequestDispatcher {

  public HelloWorldServlet() {
    setRoutes(
        resource("/", get(writeUsing(textFrom("Hello world!\n")))),

        resource("/hello",
                 get((req, res) -> {
                   String queryString = req.getQueryString();
                   res.getWriter().append("Hello ").append(decodeOrUse(queryString, "world")).append('!').println();
                 }),
                 put((req, res) -> {
                   res.getWriter().append("Hello ");
                   textFrom(req.getReader()).writeTo(res.getWriter());
                   res.getWriter().append('!').println();
                 })),

        mvc("/good-morning", new GoodMorningView(),
            get(req -> decodeOrUse(req.getQueryString(), "")),
            put(req -> req.getReader().readLine())),

        mvc("/echo", new StringView(), new EchoQueryController())
    );
  }

  static String decodeOrUse(String queryString, String fallback) throws UnsupportedEncodingException {
    return queryString != null && !queryString.isEmpty() ? URLDecoder.decode(queryString, "UTF-8") : fallback;
  }

  private class EchoQueryController extends WebMVCResource<String> {
    public EchoQueryController() {
      super(
          get(req -> decodeOrUse(req.getQueryString(), "")),
          put(req -> req.getReader().readLine()));
    }
  }

  static class GoodMorningView implements WebView<String> {
    @Override
    public void render(String model, HttpServletRequest request, HttpServletResponse response) throws Exception {
      response.setCharacterEncoding("UTF-8");
      response.setContentType("text/plain");
      PrintWriter writer = response.getWriter();
      writer.append("Good morning");
      if (!model.isEmpty()) {
        writer.append(' ').append(model);
      }
      writer.append('!').println();
    }
  }

  static class StringView implements WebView<String> {
    @Override
    public void render(String model, HttpServletRequest request, HttpServletResponse response) throws Exception {
      response.setCharacterEncoding("UTF-8");
      response.setContentType("text/plain");
      response.getWriter().write(model);
    }
  }
}
