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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** Composite (#HttpRequestFilter). */
public class HttpRequestFilterChain implements HttpRequestFilter {

  protected final List<HttpRequestFilter> chained = new ArrayList<>();

  /** request will be propagated through `chained` in iteration order. */
  public HttpRequestFilterChain(List<HttpRequestFilter> chained) {
    this.chained.addAll(chained);
  }

  @Override
  /**
   * Applies all chained filters in iteration order. If any filter stops
   * propagation, propagation is stopped for the whole chain and `next` will
   * not be invoked.
   */
  public void filter(HttpServletRequest request, HttpServletResponse response, HttpRequestHandler next) throws Exception {
    new Walker(chained.iterator(), next).handle(request, response);
  }

  /**
   * Helper class to iterate over a list of filters. Invokes filters in
   * iteration order inserting itself as `next`.
   */
  public static class Walker implements HttpRequestHandler {
    protected final Iterator<HttpRequestFilter> it;
    protected final HttpRequestHandler eoc;

    public Walker(Iterator<HttpRequestFilter> it, HttpRequestHandler eoc) {
      this.it = it;
      this.eoc = eoc;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
      if (it.hasNext()) {
        it.next().filter(request, response, this);
      } else if (eoc != null) {
        eoc.handle(request, response);
      }
    }
  }
}
