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

import java.util.function.Supplier;

public class DeploymentStatusHolder
    implements Supplier<DeploymentStatus> {

  DeploymentStatus deploymentStatus = DeploymentStatus.BOOTING;

  public DeploymentStatus get() {
    return deploymentStatus;
  }

  public void set(DeploymentStatus deploymentStatus) {
    this.deploymentStatus = deploymentStatus;
  }
}
