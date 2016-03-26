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
package org.example.key_value.status;

import org.example.key_value.repository.Repository;
import org.tinyj.web.mvc.resource.WebMVCResource;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.tinyj.web.mvc.DSL.get;
import static org.tinyj.web.mvc.DSL.put;

public class StatusController extends WebMVCResource<Status> {

  final Repository repository;
  final DeploymentStatusHolder deploymentStatusHolder;

  public StatusController(Repository repository, DeploymentStatusHolder deploymentStatusHolder) {
    this.repository = repository;
    this.deploymentStatusHolder = deploymentStatusHolder;

    setMethods(
        get(req -> getStatus()),
        put(this::setStatus));
  }

  public Status setStatus(HttpServletRequest request) throws IOException {
    String statusString = request.getReader().readLine();
    deploymentStatusHolder.set(DeploymentStatus.valueOf(statusString));
    return getStatus();
  }

  Status getStatus() {
    Status status = new Status();
    status.entryCount = repository.count();
    status.deploymentStatus = deploymentStatusHolder.get();
    return status;
  }
}
