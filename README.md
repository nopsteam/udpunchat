# udpuchat

Basic Chat using UDP Punching Hole

## Installation

Download from https://github.com/nopsteam/udpuchat

## Usage

### Server
```bash
clj -X:run-server
```

### Clients
```bash
clj -X:run-client :client-id 1
# AND
clj -X:run-client :client-id 2

# OR

clj -X:run-client :client-id 1 :server-port 7070
# AND
clj -X:run-client :client-id 2 :server-port 7070

# OR

clj -X:run-client :client-id 1 :server-port 7070 :server-ip \"127.0.0.1\"
# AND
clj -X:run-client :client-id 2 :server-port 7070 :server-ip \"127.0.0.1\"
```

### Lint & format
```bash
clj -M:clojure-lsp format
clj -M:clojure-lsp clean-ns
clj -M:clojure-lsp diagnostics
```

### Build
Run the project's tests (they'll fail until you edit them):

    $ clojure -T:build test

Run the project's CI pipeline and build an uberjar (this will fail until you edit the tests to pass):

    $ clojure -T:build ci

> This will produce an updated `pom.xml` file with synchronized dependencies inside the `META-INF`
directory inside `target/classes` and the uberjar in `target`. You can update the version (and SCM tag)
information in generated `pom.xml` by updating `build.clj`. If you don't want the `pom.xml` file in your project, you can remove it. The `ci` task will
still generate a minimal `pom.xml` as part of the `uber` task, unless you remove `version`
from `build.clj`.

Run that uberjar:

    $ java -jar target/udpuchat-0.1.0-SNAPSHOT.jar

If you remove `version` from `build.clj`, the uberjar will become `target/udpuchat.jar`.

## License

This is free and unencumbered software released into the public domain.
For more information, please refer to http://unlicense.org
