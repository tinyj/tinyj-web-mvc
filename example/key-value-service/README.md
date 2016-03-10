# key-value-service

Implementation of a simple REST conform key value store.

## Endpoints

### /status

Status information resource for our service. The service can be in one of the following
states: `BOOTING`, `RUNNING`, `PAUSED`, `SHUTTING_DOWN`.

`GET` and `PUT` request are allow, the response will contain the current service state
and the number of key/value pairs known to the service.

Example response:
```
service.status: RUNNING
repository.entry.count: 142
```

For easy integration with load balancers the status code will also indicate
the current service state.

 - in `BOOTING`, `PAUSED`, `SHUTTING_DOWN` state the response status
   is `503 Service Unavailable`
 - in `RUNNING` state the response status is `200 OK`
 - if `BOOTING`, `PAUSED` the `Retry-After` header is set.

A `PUT` put request should contain the new service state as first line of the
body. If the requested state is unknown the response status is
`400 Bad Request`.


### /store

REST endpoint to manage key/value pairs.

#### Supported methods

##### Collection operations

###### GET /store

Response will contain the URIs of all available key/value pairs. Response
status is `200 OK` or `204 No Content` if are no key/value pairs stored.

##### POST /store

Creates a new key/value pair with a random key and the request body as value.
The value is echoed in the response body, the URI for the key/value pair is
included in the `Location` header. The response status is `201 Created`.

#### Entity operations

For all operations `{key}` may be empty or contain slashes.

##### GET /store/{key}

Responds with the value of the given key or with `404 Not Found` if there is
no such key.

##### PUT /store/{key}

Stores the request body as value for the given key and echos the new value in
the response. Response status code is `201 Created` if there was no value for
the given key or `200 OK` if the value was updated.

##### DELETE /store/{key}

Deletes the given key/value pair. Response contains the deleted value.
