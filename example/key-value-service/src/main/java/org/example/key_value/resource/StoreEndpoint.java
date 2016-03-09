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
import org.tinyj.web.mvc.WebRenderer;
import org.tinyj.web.mvc.resource.WebMVCResource;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.tinyj.web.mvc.dsl.DSL.*;

public class StoreEndpoint extends WebMVCResource<WebRenderer> {

  Repository repository;

  public StoreEndpoint(Repository repository) {
    this.repository = repository;
    setMethods(
        delete(this::deleteValue),
        get(dispatch(
            route("", this::getValues),
            route("/*", this::getValue))),
        post(dispatch(route("", this::postValue), route("/*", req -> {
          throw new UnsupportedOperationException();
        }))),
        put(this::putValue)
    );
  }

  WebRenderer deleteValue(HttpServletRequest request) {
    return write(repository.delete(getKey(request)));
  }

  WebRenderer getValue(HttpServletRequest request) {
    return write(repository.get(getKey(request)));
  }

  WebRenderer getValues(HttpServletRequest request) {
    final String keyPrefix = getKey(request);
    String[] keysParams = request.getParameterValues("key");
    Set<String> keys = keysParams != null ? Arrays.stream(keysParams).map(key -> keyPrefix + key).collect(toSet())
                                          : null;
    String[] valueParams = request.getParameterValues("value");
    Set<String> values = valueParams != null ? Arrays.stream(valueParams).collect(toSet())
                                             : null;

    return write(repository.find(keys, values), strings -> String.join("\r\n", strings));
  }

  WebRenderer postValue(HttpServletRequest request) throws IOException {
    String value = request.getReader().readLine();
    final String subKey = repository.createKey(value);

    return write(value).withStatus(201).withHeader("Location", subKey);
  }

  WebRenderer putValue(HttpServletRequest request) throws IOException {
    final String value = repository.update(getKey(request), request.getReader().readLine());
    return write(value);
  }

  String getKey(HttpServletRequest request) {
    String pathInfo = request.getPathInfo();
    return pathInfo != null ? pathInfo.substring(1, pathInfo.length()) : "";
  }
}
