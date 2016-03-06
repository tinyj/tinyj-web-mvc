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
import org.tinyj.test.servlet.HttpServletResponseMock;

import static org.assertj.core.api.Assertions.assertThat;

public class BinaryRendererTest {

  static final byte[] BODY = new byte[]{0, 1, 2, 3, 4, 5, 6, 7};

  BinaryRenderer renderer;
  HttpServletResponseMock response;

  @BeforeMethod
  public void setUp() throws Exception {
    renderer = new BinaryRenderer(out -> out.write(BODY));
    response = new HttpServletResponseMock();
  }

  @Test
  public void by_default_the_content_type_is_application_octet_stream() throws Exception {

    // when
    renderer.render(response);
    response.close();

    // then
    assertThat(response.getCommitedHeaders().get("Content-Type"))
        .hasSize(1).containsExactly("application/octet-stream");
  }

  @Test
  public void the_streamer_is_applied_to_render_the_response_body() throws Exception {

    // when
    renderer.render(response);
    response.close();

    // then
    assertThat(response.getSendBodyBytes()).containsExactly(BODY);
  }
}