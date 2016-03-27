# hello-world

A few [variations](src/main/java/org/example/hello_world/HelloWorldExtended.java) on the _Hello world!_ example.

## Endpoints

### /

Responds to `GET` requests with `Hello world!`.


### /hello

_Hello!_ service implemented as request handler.

###### GET /hello
Responds with `Hello world!` if the requested without query string,
responds with `Hello <query string>!` otherwise.

###### PUT /hello
Responds with `Hello <request body>!`.


### /good-morning

_Good morning!_ service. Similar to the _Hello!_ service buy this time
implemented in a MVC fashion.

###### GET /good-morning
Responds with `Good morning!` if the requested without query string,
responds with `Good morning <query string>!` otherwise.

###### PUT /good-morning
Responds with `Good morning <request body>!`.


### /echo

_echo_ service. Uses a copy of the Controller used for the _Good morning!_
service but this time paired with another view.

###### GET /echo
Responds with the requests query string.

###### PUT /echo
Responds with the requests body.
