package org.example.key_value.resource;

import org.example.key_value.repository.Repository;
import org.tinyj.web.mvc.WebResource;
import org.tinyj.web.mvc.WebResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.tinyj.web.mvc.DSL.*;

public class KeyValueController extends WebResource<WebResponse<?>> {

  final Repository repository;

  public KeyValueController(Repository repository) {
    this.repository = repository;

    setMethods(
        delete(this::deleteValue),
        get(this::getValue),
        put(this::putValue)
    );
  }

  WebResponse<String> deleteValue(HttpServletRequest request) {
    return WebResponse.wrap(repository.delete(getKey(request)));
  }

  WebResponse<String> getValue(HttpServletRequest request) {
    return WebResponse.wrap(repository.get(getKey(request)));
  }

  WebResponse<String> putValue(HttpServletRequest request) throws IOException {
    final String value = request.getReader().readLine();
    final String oldValue = repository.update(getKey(request), value);
    final WebResponse<String> response = WebResponse.wrap(value);
    if (oldValue == null) {
      response.withStatus(201).withHeader("Location", request.getRequestURL().toString());
    }
    return response;
  }

  String getKey(HttpServletRequest request) {
    String pathInfo = request.getPathInfo();
    return pathInfo != null ? pathInfo.substring(1, pathInfo.length()) : "";
  }
}