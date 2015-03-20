package com.milandb.server.thriftscala

import com.twitter.logging.Logger
import com.twitter.util.Future

/**
 * Milan Store Service.
 */
class MilanService extends MilanStoreService[Future] {
  import scala.collection.mutable

  val log: Logger = Logger.get(getClass)

  val database = new mutable.HashMap[String, String]() //dummy

  override def get(key: String): Future[String] = {
    log.info(s"get($key)")

    database.get(key) match {
      case None => Future.exception(MilanStoreException("Key not found"))
      case Some(value) => Future(value)
    }
  }

  override def put(key: String, value: String): Future[Unit] = {
    database(key) = value
    log.info(s"put($key, $value)")
    Future.Unit
  }
}

object MilanService {
  /**
   * An asynchronous constructor that creates a `MilanService`.
   */
  def create(): Future[MilanService] = Future(new MilanService())
}