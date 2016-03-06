package org.tinyj.web.mvc;

import javax.servlet.http.HttpServletResponse;

/**
 * A `WebView` is responsible for rendering the result model of an (#WebController)
 * into a HTTP response.
 *
 * @param <T> model type.
 */
@FunctionalInterface
public interface WebView<T> {

  /**
   * Render `model` into `response`.
   *
   * By passing `response` all claims onto `response` are yielded. I.e. after
   * returning `response` may be in any state, it may or may not be committed,
   * data may or may not been send.
   *
   * Even though it's not a strict requirement from this interface, most
   * implementations will assume that prior to invocation `response` is uncommitted.
   */
  void render(T model, HttpServletResponse response) throws Exception;
}
