package org.example.servlet;/*
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

import org.glassfish.jersey.client.JerseyWebTarget;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

import static javax.ws.rs.client.Entity.text;
import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.example.key_value.status.DeploymentStatus.RUNNING;
import static org.glassfish.jersey.client.JerseyClientBuilder.createClient;

public class ServiceTestIT {

  public static final String BASE_URL = "http://localhost:8080/service";
  private JerseyWebTarget webTarget;

  @BeforeMethod
  public void setUp() throws Exception {
    webTarget = createClient().target(BASE_URL);
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
  public void posting_values() throws Exception {
    // setup
    Response response;

    // when
    response = webTarget.path("store").request().get();

    // then
    assertEmptyResponse(response);

    // when
    response = webTarget.path("store").request().post(text("posted value"));

    // then
    assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());
    assertThat(response.getLocation().toASCIIString())
        .startsWith(UriBuilder.fromUri(BASE_URL).path("store/").build().toASCIIString());
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
    response = webTarget.path("store").path("key").request().get();

    // then
    assertResponse(response, NOT_FOUND, "Not found: key\n");

    // when
    response = webTarget.path("store").path("key").request().put(text("putted value"));

    // then
    assertResponse(response, CREATED, "putted value");
    assertThat(response.getLocation().toASCIIString())
        .isEqualTo(BASE_URL + "/store/key");

    // when
    response = webTarget.path("store").path("key").request().get();

    // then
    assertResponse(response, OK, "putted value");

    // when
    response = webTarget.path("store").path("key").request().put(text("different putted value"));

    // then
    assertResponse(response, OK, "different putted value");

    // when
    response = webTarget.path("store").path("key").request().get();

    // then
    assertResponse(response, OK, "different putted value");

    // when
    response = webTarget.path("store").request().get();

    // then
    assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
    assertThat(response.readEntity(String.class).split("\r?\n")).containsOnly(BASE_URL + "/store/key");

    // when
    response = webTarget.path("store").path("key").request().delete();

    //then
    assertResponse(response, OK, "different putted value");

    // when
    response = webTarget.path("store").request().get();

    // then
    assertEmptyResponse(response);

    // when
    response = webTarget.path("store").path("key").request().get();

    // then
    assertResponse(response, NOT_FOUND, "Not found: key\n");
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