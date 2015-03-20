# Milandb

Milandb is a distributed key-value store based on Akka, CRDT and Twitter Finagle.

Quick start
-----------

Run `./sbt` from the project root, [Scrooge][1] will
generate our Thrift service and client traits, and then it'll
compile them along with the rest of our code and start a Scala console. 

Change project `project milan-core` and start console `console`.

Paste (`paste`) the following lines to start a server running locally on port 9090:

```
import com.twitter.finagle.Thrift
import com.milandb.server.thriftscala._

val server = MilanService.create map { service =>
  Thrift.serveIface("localhost:9090", service)
} onSuccess { _ =>
  println("Server started successfully")
} onFailure { ex =>
  println("Could not start the server: " + ex)
}
```

This will print following:

```
...
Server started successfully
...
```

Now you can create a client to speak to the server:

```
import com.twitter.finagle.Thrift
import com.milandb.server.thriftscala._

val client =
  Thrift.newIface[MilanStoreService.FutureIface]("localhost:9090")

  client.put("mykey", "myvalue") flatMap { putRes =>
    println("1. put: " + putRes)
    
    client.get("mykey") map { getRes =>
      println("2. get: " + getRes)
    }
  }
```

This will print the following:

```
1. put: ()
2. get: myvalue
```

[1]: http://twitter.github.io/scrooge/
