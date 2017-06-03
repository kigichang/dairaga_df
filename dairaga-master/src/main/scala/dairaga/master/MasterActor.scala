package dairaga.master

import akka.actor.{ActorRef, Props, Terminated}
import akka.cluster.pubsub.DistributedPubSubMediator.Publish
import com.sun.xml.internal.ws.developer.Serialization
import dairaga.akka._
import dairaga.env.ClusterInfo

import scala.collection.mutable

/**
  * A master actor to monitor and manage other cluster nodes.
  *
  * Created by kigi on 01/06/2017.
  */
final class MasterActor extends DairagaActor {

  /**
    * Intern actors of other nodes
    */
  private val _nodes = mutable.HashSet.empty[ActorRef]

  override def receive: Receive = {

    case XVRegister =>
      val tmp = sender()
      if (tmp != context.parent) {
        if (!_nodes.contains(sender())) {
          _nodes += tmp
          context.watch(tmp)
          log.info(s"${tmp.path.toString} reply")
        }
      }

    case XVPing =>
      mediator ! Publish(ClusterInfo, XVPing)

    case XVShutdown =>
      _nodes foreach { x =>
        x ! XVShutdown
      }

    case MasterActor.MasterList =>
      sender() ! _nodes.toSeq

    case MasterActor.MasterClose(host) =>
      _nodes.filter(_.path.toString.contains(host)).foreach(_ ! XVShutdown)

    case Terminated(terminatedActor) =>
      context.unwatch(terminatedActor)
      _nodes -= terminatedActor
  }
}

object MasterActor {
  val props = Props[MasterActor]

  @SerialVersionUID(1L) final case object MasterList
  @SerialVersionUID(1L) final case class MasterClose(host: String)
}