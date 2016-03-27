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
package org.example.key_value;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.tinyj.test.servlet.HttpServletResponseMock;

import static org.assertj.core.api.Assertions.assertThat;

public class HeadResponseTest {

  private HeadResponse headResponse;
  private HttpServletResponseMock underlyingResponse;

  @BeforeMethod
  public void setUp() throws Exception {
    underlyingResponse = new HttpServletResponseMock();
    headResponse = new HeadResponse(underlyingResponse);
  }

  @Test
  public void no_body_is_send() throws Exception {

    headResponse.getWriter().write("body text");
    headResponse.close();

    assertThat(underlyingResponse.getSendBodyBytes()).isEmpty();
  }

  @Test
  public void the_content_length_is_recorded() throws Exception {

    headResponse.getWriter().write("body text");
    headResponse.close();

    assertThat(underlyingResponse.getCommitedHeaders().get("Content-Length"))
        .hasSize(1).containsExactly("9");
  }

  @Test
  public void the_content_length_is_zero_if_message_body_was_empty() throws Exception {

    headResponse.close();

    assertThat(underlyingResponse.getCommitedHeaders().get("Content-Length"))
        .hasSize(1).containsExactly("0");
  }

  @Test
  public void output_can_be_reset() throws Exception {

    headResponse.getWriter().write("body text");
    headResponse.reset();
    headResponse.getWriter().write("another body");
    headResponse.close();

    assertThat(underlyingResponse.getSendBodyBytes()).isEmpty();
    assertThat(underlyingResponse.getCommitedHeaders().get("Content-Length"))
        .hasSize(1).containsExactly("12");
  }

  @Test
  public void headers_are_recorded() throws Exception {

    headResponse.setHeader("X-Test", "data");
    headResponse.close();

    assertThat(underlyingResponse.getCommitedHeaders().get("X-Test"))
        .hasSize(1).containsExactly("data");
  }
}
