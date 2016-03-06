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
 * The `HttpRequestHandler` is the core abstraction for tinyj-web-mvc. A
 * `HttpRequestHandler` is responsible for accepting a HTTP request and render
 * an appropriate HTTP response.
 */
@FunctionalInterface
public interface HttpRequestHandler {

  /**
   * Use `response` to render a HTTP response to `request`.
   *
   * By passing `response` all claims onto `response` are yielded. I.e. after
   * returning `response` may be in any state, it may or may not be committed,
   * data may or may not been send.
   *
   * Even though it's not a strict requirement from this interface, most
   * implementations will assume that prior to invocation `response` is uncommitted.
   */
  void handle(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
