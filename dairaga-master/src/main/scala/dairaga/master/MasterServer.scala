package dairaga.master

import akka.actor.Address
import com.google.inject.Guice
import com.typesafe.config.ConfigFactory
import dairaga.akka.ClusterNode
import dairaga.data.AkkaSeeds

import scala.collection.immutable
import scala.io.StdIn

/**
  * Created by kigi on 31/05/2017.
  */
object MasterServer {


  val injector = Guice.createInjector(new MasterModule())

  val master = new MasterNode(injector.getInstance(classOf[AkkaSeeds]))

  val url = ConfigFactory.load().getString("dairaga.data.mariadb.url")


  def main(args: Array[String]): Unit = {

    master.run()

    var cmd = StdIn.readLine("Master >>")

    while(cmd != dairaga.cmd.Quit) {
      cmd = StdIn.readLine("Master >>")
    }

    master.shutdown()
  }
}
