package dairaga.akka

import akka.actor.{ActorRef, ActorSystem, Address, Props}
import akka.cluster.Cluster
import akka.cluster.pubsub.DistributedPubSubMediator.Subscribe
import dairaga.env._

import scala.collection.immutable
/**
  * Created by kigi on 5/8/17.
  */
trait ClusterNode {

  private var _cluster: Cluster = null

  private var _system: ActorSystem = null

  private var _intern: ActorRef = null

  def seeds: immutable.Seq[Address]

  def cluster: Cluster = _cluster

  def system: ActorSystem = _system

  def inter: ActorRef = _intern

  def preStart(): Unit = Unit

  def postStop(): Unit = Unit

  def afterRun(): Unit = Unit

  def run(name: String = AkkaClusterName): Unit = {
    preStart()
    _cluster = AkkaUtils.cluster(seeds, name)
    _system = _cluster.system

    _intern = _system.actorOf(Props(new DairagaActor {

      mediator ! Subscribe(XVClusterInfo, self)

      override def receive: Receive = {
        case XVPing =>
          sender() ! XVRegister

        case XVShutdown =>
          shutdown()


      }
    }), XVInternActor)

    afterRun()
  }

  def shutdown(): Unit = {
    _cluster.leave(_cluster.selfAddress)
    _system.terminate().onComplete(_ => postStop())(_system.dispatcher)
  }
}
