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
package org.example.key_value.resource;

import org.example.key_value.repository.Repository;
import org.tinyj.web.mvc.WebResponse;
import org.tinyj.web.mvc.resource.WebMVCResource;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.tinyj.web.mvc.DSL.*;

public class StoreController extends WebMVCResource<WebResponse<?>> {

  final Repository repository;

  public StoreController(Repository repository) {
    this.repository = repository;

    setMethods(
        get(this::getValues),
        post(this::postValue),
        options(req -> WebResponse.<Void>wrap(null)
            .withHeader("Content-Length", "0")
            .withHeader("Allow", String.join(",", methods.keySet())))
    );
  }

  WebResponse<Set<String>> getValues(HttpServletRequest request) {
    final String keyPrefix = "";
    Optional<Set<String>> keys = Optional.ofNullable(request.getParameterValues("key"))
        .map(x -> Arrays.stream(x).map(key -> keyPrefix + key).collect(toSet()));
    Optional<Set<String>> values = Optional.ofNullable(request.getParameterValues("value"))
        .map(x -> Arrays.stream(x).map(key -> keyPrefix + key).collect(toSet()));

    return WebResponse.wrap(repository.findKeys(keys, values).stream()
                                .map(k -> request.getRequestURL().append('/').append(k).toString())
                                .collect(toSet()));
  }

  WebResponse<String> postValue(HttpServletRequest request) throws IOException {
    String value = request.getReader().readLine();
    final String subKey = repository.createKey(value);

    return WebResponse.wrap(value)
        .withStatus(201).withHeader("Location", request.getRequestURL().append('/').append(subKey).toString());
  }
}
