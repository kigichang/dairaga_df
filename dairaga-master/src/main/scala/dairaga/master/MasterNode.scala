package dairaga.master

import javax.inject.{Inject, Singleton}

import akka.actor.{ActorContext, ActorRef, Address}
import dairaga.akka.ClusterNode
import dairaga.data.AkkaSeeds

import scala.collection.immutable
/**
  * Created by kigi on 01/06/2017.
  */
class MasterNode (val akkaSeeds: AkkaSeeds) extends ClusterNode {
  override def seeds: immutable.Seq[Address] = akkaSeeds.seeds

  private var _master: ActorRef = null
  def master = _master

  override def afterRun(): Unit = {
    super.afterRun()
    //_master = system.actorOf(MasterActor.props, "master")
    akkaSeeds.write(cluster.selfAddress)
  }

  override def postStop(): Unit = {
    akkaSeeds.remove(cluster.selfAddress)
    super.postStop()
  }

  override def internPreStart(context: ActorContext): Unit = {
    super.internPreStart(context)
    _master = context.actorOf(MasterActor.props, "master")
  }

  override def childOnTerminated(context: ActorContext, actor: ActorRef): Unit = {
    super.childOnTerminated(context, actor)
    if (actor == _master) {
      _master = context.actorOf(MasterActor.props, "master")
    }
  }

}
