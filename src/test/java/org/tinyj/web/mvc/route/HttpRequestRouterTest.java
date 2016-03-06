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
import java.util.AbstractMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;

public class HttpRequestRouterTest {

  HttpRequestRouter<Integer> router;
  HttpRequestRouter.Routing<Integer> routing;

  @Test
  public void resources_can_be_found_by_their_target_path() throws Exception {
    // given
    router = new HttpRequestRouter<>(routes(r("/1", 1), r("/2", 2), r("/3", 3)));

    // when
    routing = router.route(request("/1"));

    // then
    assertThat(routing.routedRequest.getServletPath()).isEqualTo("/1");
    assertThat(routing.routedRequest.getPathInfo()).isNull();
    assertThat(routing.target).isEqualTo(1);

    // when
    routing = router.route(request("/2"));

    // then
    assertThat(routing.routedRequest.getServletPath()).isEqualTo("/2");
    assertThat(routing.routedRequest.getPathInfo()).isNull();
    assertThat(routing.target).isEqualTo(2);

    // when
    routing = router.route(request("/3"));

    // then
    assertThat(routing.routedRequest.getServletPath()).isEqualTo("/3");
    assertThat(routing.routedRequest.getPathInfo()).isNull();
    assertThat(routing.target).isEqualTo(3);
  }

  @Test
  public void fallback_is_provided_if_no_route_was_found() throws Exception {
    // given
    router = new HttpRequestRouter<>(routes(r("/1", 1), r("/2", 2), r("/3", 3), r("/*", 0)));

    // when
    routing = router.route(request("/4"));

    // then
    assertThat(routing.routedRequest.getServletPath()).isEqualTo("");
    assertThat(routing.routedRequest.getPathInfo()).isEqualTo("/4");
    assertThat(routing.target).isEqualTo(0);

    // when
    routing = router.route(request("/1/2"));

    // then
    assertThat(routing.routedRequest.getServletPath()).isEqualTo("");
    assertThat(routing.routedRequest.getPathInfo()).isEqualTo("/1/2");
    assertThat(routing.target).isEqualTo(0);
  }

  @Test
  public void wildcard_routes() throws Exception {
    // given
    router = new HttpRequestRouter<>(routes(r("/1/*", 1), r("/2/*", 2), r("/3", 3), r("/*", 0)));

    // when
    routing = router.route(request("/1/x"));

    // then
    assertThat(routing.routedRequest.getServletPath()).isEqualTo("/1");
    assertThat(routing.routedRequest.getPathInfo()).isEqualTo("/x");
    assertThat(routing.target).isEqualTo(1);

    // when
    routing = router.route(request("/2/x"));

    // then
    assertThat(routing.routedRequest.getServletPath()).isEqualTo("/2");
    assertThat(routing.routedRequest.getPathInfo()).isEqualTo("/x");
    assertThat(routing.target).isEqualTo(2);

    // when
    routing = router.route(request("/4/x"));

    // then
    assertThat(routing.routedRequest.getServletPath()).isEqualTo("");
    assertThat(routing.routedRequest.getPathInfo()).isEqualTo("/4/x");
    assertThat(routing.target).isEqualTo(0);

    // when
    routing = router.route(request(null));

    // then
    assertThat(routing.routedRequest.getServletPath()).isEqualTo("");
    assertThat(routing.routedRequest.getPathInfo()).isNull();
    assertThat(routing.target).isEqualTo(0);

    // when
    routing = router.route(request(""));

    // then
    assertThat(routing.routedRequest.getServletPath()).isEqualTo("");
    assertThat(routing.routedRequest.getPathInfo()).isNull();
    assertThat(routing.target).isEqualTo(0);
  }

  @Test
  public void longer_matches_win() throws Exception {
    // given
    router = new HttpRequestRouter<>(routes(r("/1/*", 1), r("/1/2/3", 3), r("/1/2/*", 2)));

    // when
    routing = router.route(request("/1/2/3"));

    // then
    assertThat(routing.target).isEqualTo(3);

    // when
    routing = router.route(request("/1/2/x"));

    // then
    assertThat(routing.target).isEqualTo(2);

    // when
    routing = router.route(request("/1/x/3"));

    // then
    assertThat(routing.target).isEqualTo(1);
  }

  /**
   * This test is for demonstration, not for specification. Trailing slashes
   * are unsupported and behavior may change in future.
   */
  @Test
  public void trailing_slashes() throws Exception {
    // given
    router = new HttpRequestRouter<>(routes(
        r("/1", 11), r("/1/", 12), r("/1/*", 13), r("/1/*/", 14), r("/1/2/3", 19),
        r("/2", 21), r("/2/*", 23), r("/3", 31), r("/3/*/", 33),
        r("/*", 0)));

    // when
    routing = router.route(request("/1/"));

    // then
    assertThat(routing.routedRequest.getServletPath()).isEqualTo("/1/");
    assertThat(routing.routedRequest.getPathInfo()).isNull();
    assertThat(routing.target).isEqualTo(12);


    // when
    routing = router.route(request("/1/2/3/"));

    // then
    assertThat(routing.routedRequest.getServletPath()).isEqualTo("/1");
    assertThat(routing.routedRequest.getPathInfo()).isEqualTo("/2/3/");
    assertThat(routing.target).isEqualTo(13);

    // when
    routing = router.route(request("/1/x/"));

    // then
    assertThat(routing.routedRequest.getServletPath()).isEqualTo("/1");
    assertThat(routing.routedRequest.getPathInfo()).isEqualTo("/x/");
    assertThat(routing.target).isEqualTo(13);

    // when
    routing = router.route(request("/3/x/"));

    // then
    assertThat(routing.routedRequest.getServletPath()).isEqualTo("");
    assertThat(routing.routedRequest.getPathInfo()).isEqualTo("/3/x/");
    assertThat(routing.target).isEqualTo(0);

    // when
    routing = router.route(request("/2/"));

    // then
    assertThat(routing.routedRequest.getServletPath()).isEqualTo("/2");
    assertThat(routing.routedRequest.getPathInfo()).isEqualTo("/");
    assertThat(routing.target).isEqualTo(23);
  }

  @SafeVarargs
  final Map<String, Integer> routes(Map.Entry<String, Integer>... routes) {
    return stream(routes).collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  Map.Entry<String, Integer> r(String target, int routed) {
    return new AbstractMap.SimpleEntry<>(target, routed);
  }

  HttpServletRequest request(String path) {
    return new HttpServletRequestMock().withPath(path);
  }
}