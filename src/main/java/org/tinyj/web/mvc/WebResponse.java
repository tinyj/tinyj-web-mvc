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

import java.util.HashMap;
import java.util.Map;

public class WebResponse<T> {

  protected int status;
  protected String contentType;
  protected String encoding;
  protected final Map<String, String[]> headers = new HashMap<>();
  protected final T model;

  /** Create new `WebResponse` for `model`. Status code defaults to `200 OK`. */
  public WebResponse(T model) {
    this.status = 200;
    this.model = model;
  }

  /** Shortcut for `new WebResponse<>(model)`. */
  public static <T> WebResponse<T> wrap(T model) {
    return new WebResponse<>(model);
  }

  /** Response will be rendered with status code `status`. */
  public WebResponse<T> withStatus(int status) {
    this.status = status;
    return this;
  }

  /**
   * Response will be rendered with the _Content-Type_ header set to `contentType`.
   * This overrides _Content-Type_ headers set with `withHeader`.
   */
  public WebResponse<T> withContentType(String contentType) {
    this.contentType = contentType;
    return this;
  }

  /**
   * When rendered `encoding` will be added to the _Content-Type_ header. If
   * the response is rendered using the respond's writer `encoding` is used as
   * character encoding.
   */
  public WebResponse<T> withEncoding(String encoding) {
    this.encoding = encoding;
    return this;
  }

  /** Response will be rendered with a `name`-header line for each passed value. */
  public WebResponse<T> withHeader(String name, String... values) {
    this.headers.put(name, values);
    return this;
  }

  /** Get response status. */
  public int getStatus() {
    return status;
  }

  /** Get response content type. */
  public String getContentType() {
    return contentType;
  }

  /** Get response encoding. */
  public String getEncoding() {
    return encoding;
  }

  /** Get response headers. */
  public Map<String, String[]> getHeaders() {
    return headers;
  }

  /** Get wrapped model. */
  public T getModel() {
    return model;
  }
}
