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
package org.tinyj.web.mvc.route;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class RoutedHttpRequest extends HttpServletRequestWrapper {

  protected final String servletPath;
  protected final String pathInfo;

  public RoutedHttpRequest(HttpServletRequest request, String servletPath, String pathInfo) {
    super(request);
    this.servletPath = servletPath;
    this.pathInfo = pathInfo;
  }

  public static HttpServletRequest routedRequest(HttpServletRequest request, String prefix) {
    String path = request.getPathInfo();
    String target = path != null && prefix.length() < path.length()
                    ? path.substring(prefix.length(), path.length())
                    : null;
    return new RoutedHttpRequest(unwrap(request), request.getServletPath() + prefix, target);
  }

  @Override
  public String getServletPath() {
    return servletPath;
  }

  @Override
  public String getPathInfo() {
    return pathInfo;
  }

  @Override
  public HttpServletRequest getRequest() {
    return (HttpServletRequest) super.getRequest();
  }

  protected static HttpServletRequest unwrap(HttpServletRequest request) {
    if (!(request instanceof RoutedHttpRequest)) {
      return request;
    }
    return unwrap(((RoutedHttpRequest) request).getRequest());
  }
}
