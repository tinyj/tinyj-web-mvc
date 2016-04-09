
# TinyJ WebMVC
[![build status](https://travis-ci.org/tinyj/tinyj-web-mvc.svg?branch=master)](https://travis-ci.org/tinyj/tinyj-web-mvc)

A container agnostic web model-view-controller framework. Request routing is
inspired by [spark](http://sparkjava.com/). Key differences are

 - TinyJ WebMVC does not take care of running an actual web server.
 - TinyJ WebMVC sticks to the servlet-api for request and response parameters.
 - TinyJ WebMVC does not take care of rendering the response.

This allows for easy integration with existing libraries and servlet containers.
Another key aspect of TinyJ WebMVC is to facilitate streaming data.  

## API documentation

You can find the API documentation [here](APIdoc.md).


## Examples

- [hello-world](example/hello-world) The obligatory _Hello world!_ example.
- [hello-world-extended](example/hello-world-extended) A few variations on the
  _Hello world!_ theme.
- [key-value-service](example/key-value-service) implementation of a simple
  REST conform key value store.

## License

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
