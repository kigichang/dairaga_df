package dairaga.akka

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import dairaga.key._
import dairaga.env._

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.control.NonFatal

/**
  * Created by kigi on 5/8/17.
  */
trait HttpServer extends ClusterNode {

  private var _binding: Future[Http.ServerBinding] = null

  val route: Route

  override def run(resourceName: String): Unit = {
    super.run(resourceName)

    implicit val _system: ActorSystem = system
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    val config = system.settings.config

    val host = if (config.hasPath(HttpIp)) config.getString(HttpIp) else cluster.selfAddress.host.getOrElse("127.0.0.1")

    val port = if (config.hasPath(HttpPort)) config.getInt(HttpPort) else XVHttpPort

    _binding = Http().bindAndHandle(route, host, port)
  }


  override def shutdown(): Unit = {
    _binding.flatMap(_.unbind())(system.dispatcher).onComplete(_ => super.shutdown())(system.dispatcher)
  }
}
