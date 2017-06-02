package dairaga.akka

import akka.cluster.Cluster
import dairaga.config.DairagaConfig
import dairaga.env._
import dairaga.key._
import java.net.NetworkInterface

import akka.actor.{ActorSystem, Address, Terminated}
import com.typesafe.config.{Config, ConfigFactory, ConfigValueFactory}

import scala.collection.immutable
import scala.collection.JavaConverters._
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.util.Random

/**
  * Created by kigi on 08/05/2017.
  */
object AkkaUtils {

  protected def address(config: Config): Option[String] = {
    if (config.hasPath(XVNetworkAutoDetect) && config.getBoolean(XVNetworkAutoDetect)) {
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

  protected def host(config: Config): String = address(config).getOrElse(
    if(config.hasPath(ServerIp)) config.getString(ServerIp)
    else address(config).getOrElse("127.0.0.1")
  )


  protected def port(config: Config): Int =
    if (config.hasPath(ServerPort)) config.getInt(ServerPort)
    else 30000 + Random.nextInt(20000)


  def loadConfig(name: String): Config = {
    val config = if (name == "") DairagaConfig.load else DairagaConfig.load(name)

    val akkaConfig =
      ConfigFactory.parseString(s"akka.remote.netty.tcp.port=${port(config)}")
        .withValue("akka.remote.netty.tcp.hostname", ConfigValueFactory.fromAnyRef(host(config)))

    DairagaConfig.resolve(akkaConfig, config)
  }

  def cluster(seeds: immutable.Seq[Address], config: Config): Cluster = {
    val system = ActorSystem(AkkaClusterName, config)
    val cluster = Cluster(system)

    val joinSeeds = if (seeds.nonEmpty) seeds else immutable.Seq(cluster.selfAddress)

    cluster.joinSeedNodes(joinSeeds)

    cluster
  }

  def cluster(seeds: immutable.Seq[Address], resourceName: String = ""): Cluster = cluster(seeds, loadConfig(resourceName))

  def close(cluster: Cluster): Future[Terminated] = {
    cluster.leave(cluster.selfAddress)
    cluster.system.terminate()
  }


  def shutdown(cluster: Cluster, atMost: Duration = TerminateWait): Terminated = {
    Await.result(close(cluster), atMost)
  }

}
