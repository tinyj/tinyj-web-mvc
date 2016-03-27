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

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.glassfish.jersey.client.JerseyWebTarget;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;

import static javax.ws.rs.client.Entity.text;
import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.example.key_value.status.DeploymentStatus.RUNNING;
import static org.glassfish.jersey.client.JerseyClientBuilder.createClient;

public class ServiceTestIT {

  private JerseyWebTarget webTarget;

  private Server server;
  private String baseUri;

  @BeforeClass
  public void setUp() throws Exception {
    server = new Server() {
      private Servlet servlet = new Servlet();

      @Override
      public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
          servlet.service(request, response);
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
    webTarget = createClient().target(baseUri);
  }

  @AfterClass(alwaysRun = true)
  public void tearDown() throws Exception {
    server.stop();
  }

  @Test
  public void get_status__put_status() throws Exception {
    // setup
    Response response;

    // when
    response = webTarget.path("status").request().get();

    // then
    assertThat(response.getStatus()).isEqualTo(SERVICE_UNAVAILABLE.getStatusCode());
    assertThat(response.readEntity(String.class)).startsWith("service.state: BOOTING");

    // when
    response = webTarget.path("status").request().put(text(RUNNING.name()));

    // then
    assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
    assertThat(response.readEntity(String.class)).startsWith("service.state: RUNNING");
  }

  @Test(dependsOnMethods = "get_status__put_status")
  public void head_status() throws Exception {
    // setup
    Response response;

    // when
    response = webTarget.path("status").request().head();

    // then
    assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
    assertThat(response.getHeaderString("Content-Length")).isEqualTo("51");
    assertThat(response.readEntity(String.class)).isEmpty();
  }

  @Test(dependsOnMethods = "get_status__put_status")
  public void posting_values() throws Exception {
    // setup
    Response response;

    // when
    response = webTarget.path("store").request().get();

    // then
    assertEmptyResponse(response);

    // when
    response = webTarget.path("store").request().head();

    // then
    assertEmptyResponse(response);

    // when
    response = webTarget.path("store").request().post(text("posted value"));

    // then
    assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());
    assertThat(response.getLocation().toASCIIString())
        .startsWith(UriBuilder.fromUri(baseUri).path("store/").build().toASCIIString());
    assertThat(response.readEntity(String.class)).isEqualTo("posted value");

    // given
    URI uri = response.getLocation();

    // when
    response = createClient().target(uri).request().get();

    //then
    assertResponse(response, OK, "posted value");

    // when
    response = webTarget.path("store").request().get();

    // then
    assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
    assertThat(response.readEntity(String.class).split("\r?\n")).contains(uri.toASCIIString());

    // when
    response = webTarget.path("store").request().head();

    // then
    assertResponse(response, OK, "");
    assertThat(response.getHeaderString("Content-Length")).isEqualTo(Integer.toString(uri.toASCIIString().length() + 2));

    // when
    response = createClient().target(uri).request().delete();

    //then
    assertResponse(response, OK, "posted value");

    // when
    response = webTarget.path("store").request().get();

    // then
    assertEmptyResponse(response);
  }

  @Test(dependsOnMethods = "posting_values")
  public void putting_values() throws Exception {
    // setup
    Response response;

    // when
    response = webTarget.path("store").request().get();

    // then
    assertEmptyResponse(response);

    // when
    response = webTarget.path("store/key").request().get();

    // then
    assertResponse(response, NOT_FOUND, "Not found: key\n");

    // when
    response = webTarget.path("store/key").request().head();

    // then
    assertResponse(response, NOT_FOUND, "");

    // when
    response = webTarget.path("store/key").request().put(text("putted value"));

    // then
    assertResponse(response, CREATED, "putted value");
    assertThat(response.getLocation().toASCIIString())
        .isEqualTo(baseUri + "/store/key");

    // when
    response = webTarget.path("store/key").request().get();

    // then
    assertResponse(response, OK, "putted value");

    // when
    response = webTarget.path("store/key").request().head();

    // then
    assertResponse(response, OK, "");
    assertThat(response.getHeaderString("Content-Length")).isEqualTo("12");

    // when
    response = webTarget.path("store/key").request().put(text("different putted value"));

    // then
    assertResponse(response, OK, "different putted value");

    // when
    response = webTarget.path("store/key").request().get();

    // then
    assertResponse(response, OK, "different putted value");


    // when
    response = webTarget.path("store/key").request().head();

    // then
    assertResponse(response, OK, "");
    assertThat(response.getHeaderString("Content-Length")).isEqualTo("22");

    // when
    response = webTarget.path("store").request().get();

    // then
    assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
    assertThat(response.readEntity(String.class).split("\r?\n")).containsOnly(baseUri + "/store/key");

    // when
    response = webTarget.path("store/key").request().delete();

    // then
    assertResponse(response, OK, "different putted value");

    // when
    response = webTarget.path("store").request().get();

    // then
    assertEmptyResponse(response);

    // when
    response = webTarget.path("store/key").request().get();

    // then
    assertResponse(response, NOT_FOUND, "Not found: key\n");
  }

  @Test
  public void options_for_collection() throws Exception {

    final Response response = webTarget.path("store").request().options();

    assertResponse(response, OK, "");
    assertThat(response.getHeaderString("Content-Length")).contains("0");
    assertThat(response.getHeaderString("Allow").split(" *, *")).containsOnly("GET", "HEAD", "OPTIONS", "POST");
  }

  @Test
  public void method_not_allowed_response_for_collection() throws Exception {

    final Response response = webTarget.path("store").request().delete();

    assertResponse(response, METHOD_NOT_ALLOWED, "");
    assertThat(response.getHeaderString("Content-Length")).contains("0");
    assertThat(response.getHeaderString("Allow").split(" *, *")).containsOnly("GET", "HEAD", "OPTIONS", "POST");
  }


  @Test
  public void options_for_key() throws Exception {

    final Response response = webTarget.path("store/key").request().options();

    assertResponse(response, OK, "");
    assertThat(response.getHeaderString("Content-Length")).contains("0");
    assertThat(response.getHeaderString("Allow").split(" *, *")).containsOnly("GET", "HEAD", "OPTIONS", "PUT", "DELETE");
  }

  @Test
  public void method_not_allowed_response_for_key() throws Exception {

    final Response response = webTarget.path("store/key").request().post(text(""));

    assertResponse(response, METHOD_NOT_ALLOWED, "");
    assertThat(response.getHeaderString("Content-Length")).contains("0");
    assertThat(response.getHeaderString("Allow").split(" *, *")).containsOnly("GET", "HEAD", "OPTIONS", "PUT", "DELETE");
  }

  private void assertResponse(Response response, Response.Status status, String messageBody) {
    assertThat(response.getStatus()).isEqualTo(status.getStatusCode());
    assertThat(response.readEntity(String.class)).isEqualTo(messageBody);
  }

  private void assertEmptyResponse(Response response) {
    assertThat(response.getStatus()).isEqualTo(NO_CONTENT.getStatusCode());
    assertThat(response.readEntity(String.class)).isNullOrEmpty();
  }
}