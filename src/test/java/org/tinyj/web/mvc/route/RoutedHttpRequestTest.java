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

import org.testng.annotations.Test;
import org.tinyj.test.servlet.HttpServletRequestMock;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

public class RoutedHttpRequestTest {

  @Test
  public void prefix_is_removed_from_pathInfo_and_appended_to_servletPath() throws Exception {
    // given
    HttpServletRequestMock in = new HttpServletRequestMock()
                                    .withContextPath("/context")
                                    .withServletPath("/1/2")
                                    .withPath("/3/4/5/6");

    // when
    HttpServletRequest out = RoutedHttpRequest.routedRequest(in, "/3/4");

    // then
    assertThat(out.getContextPath()).isEqualTo("/context");
    assertThat(out.getServletPath()).isEqualTo("/1/2/3/4");
    assertThat(out.getPathInfo()).isEqualTo("/5/6");
  }

  @Test
  public void in_pathInfo_may_be_null() throws Exception {
    // given
    HttpServletRequestMock in = new HttpServletRequestMock()
                                    .withContextPath("/context")
                                    .withServletPath("/1/2")
                                    .withPath(null);

    // when
    HttpServletRequest out = RoutedHttpRequest.routedRequest(in, "");

    // then
    assertThat(out.getContextPath()).isEqualTo("/context");
    assertThat(out.getServletPath()).isEqualTo("/1/2");
    assertThat(out.getPathInfo()).isEqualTo(null);
  }

  @Test
  public void out_pathInfo_becomes_null__if_request_is_fully_routed() throws Exception {
    // given
    HttpServletRequestMock in = new HttpServletRequestMock()
                                    .withContextPath("/context")
                                    .withServletPath("/1/2")
                                    .withPath("/3/4");

    // when
    HttpServletRequest out = RoutedHttpRequest.routedRequest(in, "/3/4");

    // then
    assertThat(out.getContextPath()).isEqualTo("/context");
    assertThat(out.getServletPath()).isEqualTo("/1/2/3/4");
    assertThat(out.getPathInfo()).isEqualTo(null);
  }
}