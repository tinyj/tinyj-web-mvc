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
package org.tinyj.web.mvc.resource;

import java.util.Collection;

/**
 * Indicates that a HTTP request could not be answered because the request
 * method is not allowed.
 */
public class MethodNotAllowedException extends RuntimeException {

  private final String[] allowed;

  public MethodNotAllowedException(Collection<String> allowed) {
    this.allowed = allowed.stream().toArray(String[]::new);
  }

  /**
   * List of supported methods supported by the HTTP resource.
   */
  public String[] getAllowed() {
    return allowed;
  }
}
