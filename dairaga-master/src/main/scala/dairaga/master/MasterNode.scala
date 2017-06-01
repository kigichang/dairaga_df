package dairaga.master

import javax.inject.{Inject, Singleton}

import akka.actor.Address
import dairaga.akka.ClusterNode
import dairaga.data.AkkaSeeds

import scala.collection.immutable
/**
  * Created by kigi on 01/06/2017.
  */
class MasterNode (val akkaSeeds: AkkaSeeds) extends ClusterNode {
  override def seeds: immutable.Seq[Address] = akkaSeeds.seeds

  override def afterRun(): Unit = {
    super.afterRun()
    akkaSeeds.write(cluster.selfAddress)
  }

  override def postStop(): Unit = {
    akkaSeeds.remove(cluster.selfAddress)
    super.postStop()
  }

}
