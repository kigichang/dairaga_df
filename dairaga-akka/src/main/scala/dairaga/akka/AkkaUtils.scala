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
  * Akka utilities for dairaga Akka
  *
  * Created by kigi on 08/05/2017.
  */
object AkkaUtils {

  /**
    * Auto-detect host ip via network interface name.
    *
    * The network interface name must be defined in configuration file.
    *
    * The default values are "eth0", "en0", "eno1" and "ens3".
    *
    * @param config typesafe config
    * @return Some of host ip or None
    */
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

  /**
    * Retrieve host ip.
    *
    * If host ip is not defined in configuration file, auto-detect with network interface name or return 127.0.0.1
    *
    * @param config
    * @return host ip or 127.0.0.1
    */
  protected def host(config: Config): String = if(config.hasPath(ServerIp)) config.getString(ServerIp)
    else address(config).getOrElse("127.0.0.1")


  /**
    * Retrieve port.
    *
    * If port is not defined in configuration file, return a random number in [30000, 50000).
    *
    * @param config
    * @return port number
    */
  protected def port(config: Config): Int =
    if (config.hasPath(ServerPort)) config.getInt(ServerPort)
    else 30000 + Random.nextInt(20000)


  /**
    * Load configuration for akka.
    *
    * It auto-adds akka host and port into configuration.
    *
    * @param resourceName
    * @return
    */
  def loadConfig(resourceName: String): Config = {
    val config = if (resourceName == "") DairagaConfig.load else DairagaConfig.load(resourceName)

    val akkaConfig =
      ConfigFactory.parseString(s"akka.remote.netty.tcp.port=${port(config)}")
        .withValue("akka.remote.netty.tcp.hostname", ConfigValueFactory.fromAnyRef(host(config)))

    DairagaConfig.resolve(akkaConfig, config)
  }

  /**
    * Create an akka cluster node.
    *
    * The node will join seeds if seeds is not empty, or it is as a seed.
    *
    * @param seeds Existing akka cluster seeds
    * @param config
    * @return a akka cluster node
    */
  def cluster(seeds: immutable.Seq[Address], config: Config): Cluster = {
    val system = ActorSystem(AkkaClusterName, config)
    val cluster = Cluster(system)

    val joinSeeds = if (seeds.nonEmpty) seeds else immutable.Seq(cluster.selfAddress)

    cluster.joinSeedNodes(joinSeeds)

    cluster
  }

  /**
    * @see cluster
    *
    * @param seeds
    * @param resourceName
    * @return
    */
  def cluster(seeds: immutable.Seq[Address], resourceName: String = ""): Cluster = cluster(seeds, loadConfig(resourceName))

  /**
    * Close an akka cluster node
    *
    * @param cluster
    * @return
    */
  def close(cluster: Cluster): Future[Terminated] = {
    cluster.leave(cluster.selfAddress)
    cluster.system.terminate()
  }


  /**
    * Close an akka cluster node
    *
    * @param cluster
    * @return
    */
  def shutdown(cluster: Cluster, atMost: Duration = TerminateWait): Terminated = {
    Await.result(close(cluster), atMost)
  }

}
