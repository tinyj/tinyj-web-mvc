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
package org.tinyj.web.mvc.filter;

import org.tinyj.web.mvc.HttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A HTTP request can be propagated through on ore more `HttpRequestFilter`
 * instances. Each of them may prepare the response (like pre-setting headers),
 * trigger side effects (like request logging) and enrich the request and
 * response objects as they see fit.
 */
public
@FunctionalInterface
interface HttpRequestFilter {

  /** An implementation can stop request propagation by not invoking `next`. */
  void filter(HttpServletRequest request, HttpServletResponse response, HttpRequestHandler next) throws Exception;

  /**
   * terminates the filter chain with `eoc`. Returns the resulting
   * (#HttpRequestHandler).
   */
  default HttpRequestHandler terminate(final HttpRequestHandler eoc) {
    return ((request, response) -> this.filter(request, response, eoc));
  }
}
