package dairaga.master

import akka.actor.Address
import dairaga.akka.ClusterNode

import scala.collection.immutable

/**
  * Created by kigi on 31/05/2017.
  */
object MasterServer extends ClusterNode {
  override def seeds: immutable.Seq[Address] = immutable.Seq.empty[Address]



  def main(args: Array[String]): Unit = {
    run()

    Console.readLine()
  }
}
