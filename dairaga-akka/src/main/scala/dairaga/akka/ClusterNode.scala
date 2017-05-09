package dairaga.akka

import akka.actor.{ActorSystem, Address}
import akka.cluster.Cluster

import scala.collection.immutable
/**
  * Created by kigi on 5/8/17.
  */
trait ClusterNode {

  private var _cluster: Cluster = null

  private var _system: ActorSystem = null

  def seeds: immutable.Seq[Address]

  def cluster: Cluster = _cluster

  def system: ActorSystem = _system

  def preStart(): Unit = Unit

  def postStop(): Unit = Unit

  def afterRun(): Unit = Unit

  def run(): Unit = {
    preStart()
    _cluster = AkkaUtils.cluster(seeds)
    _system = _cluster.system
    afterRun()
  }

  def shutdown(): Unit = {
    AkkaUtils.shutdown(_cluster)
    postStop()
  }
}
