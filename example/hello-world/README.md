# hello-world

The obligatory _Hello world!_ example.

## Endpoints

### /

Responds to `GET` requests with `Hello world!`.


### /hello

_Hello!_ service.

###### GET /hello
Responds with `Hello world!` if the requested without query string,
responds with `Hello <query string>!` otherwise.

###### PUT /hello
Responds with `Hello <request body>!`.


### /good-morning

_Good morning!_ service.

###### GET /good-morning
Responds with `Good morning!` if the requested without query string,
responds with `Good morning <query string>!` otherwise.

###### PUT /good-morning
Responds with `Good morning <request body>!`.


### /echo

_echo_ service.

###### GET /echo
Responds with the requests query string.

###### PUT /echo
Responds with the requests body.
