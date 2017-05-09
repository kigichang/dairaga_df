package dairaga.akka

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.sun.net.httpserver.HttpServer
import dairaga.key._

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.control.NonFatal

/**
  * Created by kigi on 5/8/17.
  */
trait HttpServer extends ClusterNode {

  private var _binding: Future[Http.ServerBinding] = null

  val route: Route

  override def run(): Unit = {
    super.run()

    implicit val system: ActorSystem = super.system
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    val config = system.settings.config

    val host = try {
      config.getString(HttpIp)
    }
    catch {
      case NonFatal(_) => cluster.selfAddress.host.getOrElse("127.0.0.1")
    }

    val port = try {
      config.getInt(HttpPort)
    }
    catch {
      case NonFatal(_) => 8080
    }

    _binding = Http().bindAndHandle(route, host, port)
  }


  override def shutdown(): Unit = {
    _binding.flatMap(_.unbind())(system.dispatcher).onComplete(_ => super.shutdown())(system.dispatcher)
  }
}
