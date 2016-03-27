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

import org.tinyj.web.mvc.HttpServletAdapter;

import javax.servlet.annotation.WebServlet;

import static org.tinyj.web.mvc.DSL.*;
import static org.tinyj.web.mvc.render.Texter.textFrom;

@WebServlet("/")
public class HelloWorldServlet extends HttpServletAdapter {

  public HelloWorldServlet() {
    super(dispatch(
        resource("/", get(
            writeUsing(textFrom("Hello world!\n"))))));
  }
}
