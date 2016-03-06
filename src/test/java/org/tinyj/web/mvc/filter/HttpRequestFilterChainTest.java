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
package org.tinyj.web.mvc.filter;

import org.mockito.InOrder;
import org.mockito.stubbing.Answer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.tinyj.web.mvc.HttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.mockito.Mockito.*;

public class HttpRequestFilterChainTest {

  private HttpRequestFilterChain filterChain;
  private HttpRequestHandler finalHandler;
  private HttpServletRequest baseRequest;
  private HttpServletResponse baseResponse;

  @BeforeMethod
  public void setUp() throws Exception {
    finalHandler = mock(HttpRequestHandler.class);
    baseRequest = mock(HttpServletRequest.class);
    baseResponse = mock(HttpServletResponse.class);
  }

  @Test
  public void next_is_called_on_empty_chain() throws Exception {
    // given
    filterChain = new HttpRequestFilterChain(emptyList());

    // when
    filterChain.filter(baseRequest, baseResponse, finalHandler);

    // then
    verify(finalHandler).handle(baseRequest, baseResponse);
  }

  @Test
  public void filters_are_called_inOrder() throws Exception {
    // setup
    HttpRequestFilter filter1 = spy(HttpRequestFilter.class, (req, res, next) -> next.handle(req, res));
    HttpRequestFilter filter2 = spy(HttpRequestFilter.class, (req, res, next) -> next.handle(req, res));
    HttpRequestFilter filter3 = spy(HttpRequestFilter.class, (req, res, next) -> next.handle(req, res));

    // given
    filterChain = new HttpRequestFilterChain(asList(filter1, filter2, filter3));

    // when
    filterChain.filter(baseRequest, baseResponse, finalHandler);

    //then
    InOrder inOrder = inOrder(filter1, filter2, filter3, finalHandler);
    inOrder.verify(filter1).filter(same(baseRequest), same(baseResponse), any(HttpRequestHandler.class));
    inOrder.verify(filter2).filter(same(baseRequest), same(baseResponse), any(HttpRequestHandler.class));
    inOrder.verify(filter3).filter(same(baseRequest), same(baseResponse), any(HttpRequestHandler.class));
    inOrder.verify(finalHandler).handle(same(baseRequest), same(baseResponse));
  }

  @Test
  public void transformed_requests_are_passed_down() throws Exception {
    // setup
    HttpServletRequest transformed1 = mock(HttpServletRequest.class);
    HttpServletRequest transformed2 = mock(HttpServletRequest.class);
    HttpRequestFilter filter1 = spy(HttpRequestFilter.class, (req, res, next) -> next.handle(transformed1, baseResponse));
    HttpRequestFilter filter2 = spy(HttpRequestFilter.class, (req, res, next) -> next.handle(transformed2, baseResponse));

    // given
    filterChain = new HttpRequestFilterChain(asList(filter1, filter2));

    // when
    filterChain.filter(baseRequest, baseResponse, finalHandler);

    //then
    InOrder inOrder = inOrder(filter1, filter2, finalHandler);
    inOrder.verify(filter1).filter(same(baseRequest), same(baseResponse), any(HttpRequestHandler.class));
    inOrder.verify(filter2).filter(same(transformed1), same(baseResponse), any(HttpRequestHandler.class));
    inOrder.verify(finalHandler).handle(same(transformed2), same(baseResponse));
  }

  @Test
  public void transformed_responses_are_passed_down() throws Exception {
    // setup
    HttpServletResponse transformed1 = mock(HttpServletResponse.class);
    HttpServletResponse transformed2 = mock(HttpServletResponse.class);
    HttpRequestFilter filter1 = spy(HttpRequestFilter.class, (req, res, next) -> next.handle(baseRequest, transformed1));
    HttpRequestFilter filter2 = spy(HttpRequestFilter.class, (req, res, next) -> next.handle(baseRequest, transformed2));

    // given
    filterChain = new HttpRequestFilterChain(asList(filter1, filter2));

    // when
    filterChain.filter(baseRequest, baseResponse, finalHandler);

    //then
    InOrder inOrder = inOrder(filter1, filter2, finalHandler);
    inOrder.verify(filter1).filter(same(baseRequest), same(baseResponse), any(HttpRequestHandler.class));
    inOrder.verify(filter2).filter(same(baseRequest), same(transformed1), any(HttpRequestHandler.class));
    inOrder.verify(finalHandler).handle(same(baseRequest), same(transformed2));
  }

  protected <T, U extends T> T spy(Class<T> clazz, U delegate) {
    return mock(clazz, delegateTo(delegate));
  }

  protected <T> Answer delegateTo(T delegate) {
    return invocation -> invocation.getMethod().invoke(delegate, invocation.getArguments());
  }
}