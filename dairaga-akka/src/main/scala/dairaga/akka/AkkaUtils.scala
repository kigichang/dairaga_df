package dairaga.akka

import akka.cluster.Cluster
import dairaga.config.DairagaConfig
import dairaga.env._
import dairaga.key._
import java.net.NetworkInterface

import akka.actor.{ActorSystem, AddressFromURIString}
import com.typesafe.config.{Config, ConfigFactory, ConfigValueFactory}

import scala.collection.immutable
import scala.collection.JavaConverters._
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.Random
import scala.util.control.NonFatal

/**
  * Created by kigi on 08/05/2017.
  */
object AkkaUtils {

  protected def address(config: Config): Option[String] = {
    if (config.getBoolean(XVNetworkAutoDetect)) {
      val candidate = config.getStringList(XVNetworkInterfaces)

      enumerationAsScalaIterator(NetworkInterface.getNetworkInterfaces).find(x =>
        candidate.contains(x.getName)
      ).flatMap(x =>
        enumerationAsScalaIterator(x.getInetAddresses)
          .find(_.isSiteLocalAddress)
          .map(_.getHostAddress)
      )
    } else None
  }

  protected def host(config: Config): String = {
    try {
      val candidate = address(config)
      if (candidate.isDefined) candidate.get else config.getString(ServerIp)
    }
    catch {
      case NonFatal(_) => "127.0.0.1"
    }
  }

  protected def port(config: Config): Int = {
    try {
      config.getInt(ServerPort)
    }
    catch {
      case NonFatal(_) => 30000 + Random.nextInt(10000)
    }
  }

  def loadConfig(file: String): Config = {
    val config = if (file == "") DairagaConfig.load else DairagaConfig.load(file)

    val akkaConfig =
      ConfigFactory.parseString(s"akka.remote.netty.tcp.port=${port(config)}")
        .withValue("akka.remote.netty.tcp.hostname", ConfigValueFactory.fromAnyRef(host(config)))

    DairagaConfig.resolve(akkaConfig, config)
  }

  def cluster(seeds: immutable.Seq[String], file: String = ""): Cluster = {
    val system = ActorSystem(AkkaClusterName, loadConfig(file))
    val cluster = Cluster(system)

    val joinSeeds = if (seeds.nonEmpty) seeds.map(AddressFromURIString.parse(_)) else immutable.Seq(cluster.selfAddress)

    cluster.joinSeedNodes(joinSeeds)

    cluster
  }

  def shutdown(cluster: Cluster, atMost: Duration = TerminateWait) = {
    cluster.leave(cluster.selfAddress)
    Await.result(cluster.system.terminate(), atMost)
  }

}
