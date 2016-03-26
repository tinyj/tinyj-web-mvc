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
package org.example.hello_world;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.JerseyInvocation;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static javax.ws.rs.client.Entity.text;
import static org.assertj.core.api.Assertions.assertThat;

public class HelloWorldServletTestIT {

  private Server server;
  private String baseUri;

  @BeforeClass
  public void setUp() throws Exception {
    server = new Server() {
      private HelloWorldServlet servlet = new HelloWorldServlet();

      @Override
      public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
          servlet.handle(request, response);
          baseRequest.setHandled(true);
        } catch (RuntimeException | IOException | ServletException e) {
          throw e;
        } catch (Exception e) {
          throw new ServletException(e);
        }
      }
    };
    ServerConnector connector = new ServerConnector(server);
    connector.setHost("127.0.0.1");
    server.addConnector(connector);
    server.start();
    baseUri = "http://127.0.0.1:" + connector.getLocalPort();
  }

  @AfterClass(alwaysRun = true)
  public void tearDown() throws Exception {
    server.stop();
  }

  @Test
  public void get_root() throws Exception {

    Response response = request("/").get();

    assertThat(response.readEntity(String.class)).isEqualTo("Hello world!\n");
  }

  @Test
  public void get_hello() throws Exception {

    Response response = request("/hello?John").get();

    assertThat(response.readEntity(String.class)).isEqualTo("Hello John!\n");
  }

  @Test
  public void put_hello() throws Exception {

    Response response = request("/hello").put(text("John"));

    assertThat(response.readEntity(String.class)).isEqualTo("Hello John!\n");
  }

  @Test
  public void get_good_morning() throws Exception {

    Response response = request("/good-morning?John").get();

    assertThat(response.readEntity(String.class)).isEqualTo("Good morning John!\n");
  }

  @Test
  public void put_good_morning() throws Exception {

    Response response = request("/good-morning").put(text("John"));

    assertThat(response.readEntity(String.class)).isEqualTo("Good morning John!\n");
  }

  @Test
  public void get_echo() throws Exception {

    Response response = request("/echo?...").get();

    assertThat(response.readEntity(String.class)).isEqualTo("...");
  }

  @Test
  public void put_echo() throws Exception {

    Response response = request("/echo").put(text("..."));

    assertThat(response.readEntity(String.class)).isEqualTo("...");
  }

  JerseyInvocation.Builder request(String requestPathAndQuery) {
    return JerseyClientBuilder.createClient().target(baseUri + requestPathAndQuery).request();
  }
}