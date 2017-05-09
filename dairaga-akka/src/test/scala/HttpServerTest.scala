import akka.actor.Address
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import dairaga.akka.HttpServer
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.collection.immutable
import scala.sys.process._

/**
  * Created by kigi on 09/05/2017.
  */
class HttpServerTest extends FlatSpec with Matchers with BeforeAndAfterAll {

  val http = new HttpServer {

    override val route: Route = get {
      complete("hello")
    }

    override def seeds: immutable.Seq[Address] = immutable.Seq.empty[Address]
  }

  "a simple http server" should "echo hello" in {
    val line = Process("curl --include http://127.0.0.1:8080/").lineStream.mkString

    line should endWith ("hello")
  }

  override def beforeAll(): Unit = {
    http.run()
  }

  override def afterAll(): Unit = {
    http.shutdown()
  }
}
