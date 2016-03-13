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
package org.tinyj.web.mvc.render;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.tinyj.test.servlet.HttpServletRequestMock;
import org.tinyj.test.servlet.HttpServletResponseMock;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;

public class TextRendererTest {

  public static final String BODY = "some ¤ text";

  private TextRenderer renderer;
  private HttpServletResponseMock response;
  private HttpServletRequest request;

  @BeforeMethod
  public void setUp() throws Exception {
    renderer = new TextRenderer(out -> out.write(BODY));
    response = new HttpServletResponseMock();
    request = new HttpServletRequestMock();
  }

  @Test
  public void by_default_the_content_type_is_text_plain__charset_utf8() throws Exception {
    // when
    renderer.handle(request, response);
    response.close();

    // then
    assertThat(response.getCommitedHeaders().get("Content-Type"))
        .hasSize(1).containsExactly("text/plain; charset=UTF-8");
  }

  @Test
  public void the_streamer_is_applied_to_render_the_response_body() throws Exception {
    // when
    renderer.handle(request, response);
    response.close();

    // then
    assertThat(response.getSendBodyBytes()).containsExactly(BODY.getBytes(Charset.forName("UTF-8")));
  }

  @Test
  public void the_given_encoding_is_applied() throws Exception {
    // given
    renderer.withEncoding("ISO-8859-1");

    // when
    renderer.handle(request, response);
    response.close();

    // then
    assertThat(response.getCommitedHeaders().get("Content-Type"))
        .hasSize(1).containsExactly("text/plain; charset=ISO-8859-1");
    assertThat(response.getSendBodyBytes()).containsExactly(BODY.getBytes(Charset.forName("ISO-8859-1")));
  }
}