package com.milandb.server

import com.twitter.finagle.Thrift
import com.twitter.server.TwitterServer
import com.twitter.util.Await
import com.milandb.server.thriftscala.MilanService

/**
 * Milan server node app.
 */
object MilanServer extends TwitterServer {
  val service = MilanService.create

  def main() {
    val server = Thrift.serveIface("localhost:9090", Await.result(service))

    onExit {
      server.close()
    }

    Await.ready(server)
  }
}
