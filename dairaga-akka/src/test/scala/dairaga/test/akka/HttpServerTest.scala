package dairaga.test.akka

import akka.actor.Address
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import dairaga.akka.HttpServer
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.collection.immutable
import scala.sys.process._

object HttpServerTest extends HttpServer {
  override val route: Route = get {
    complete("hello")
  }

  override def seeds: immutable.Seq[Address] = immutable.Seq.empty[Address]
}

/**
  * Created by kigi on 09/05/2017.
  */
class HttpServerTest extends FlatSpec with Matchers with BeforeAndAfterAll {

  "a simple http server" should "echo hello" in {
    val line = Process(s"curl --include http://${HttpServerTest.cluster.selfAddress.host.get}:15080/").lineStream.mkString

    line should endWith ("hello")
  }

  override def beforeAll(): Unit = {
    HttpServerTest.run("")
  }

  override def afterAll(): Unit = {
    HttpServerTest.shutdown()
  }
}
