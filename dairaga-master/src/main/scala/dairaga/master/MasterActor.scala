package dairaga.master

import akka.actor.{ActorRef, Props, Terminated}
import akka.cluster.pubsub.DistributedPubSubMediator.Publish
import com.sun.xml.internal.ws.developer.Serialization
import dairaga.akka._
import dairaga.env.XVClusterInfo

import scala.collection.mutable

/**
  * Created by kigi on 01/06/2017.
  */
final class MasterActor extends DairagaActor {

  private[dairaga] val actors = mutable.HashSet.empty[ActorRef]

  override def receive: Receive = {
    case XVRegister =>
      if (sender() != context.parent) {
        if (!actors.contains(sender())) {
          actors += sender()
          context.watch(sender())
          log.info(s"${sender().path.toString} reply")
        }
      }

    case XVHeartBeat =>
      mediator ! Publish(XVClusterInfo, XVPing)

    case XVShutdown =>
      actors foreach { x =>
        x ! XVShutdown
      }

    case MasterActor.MasterList =>
      sender() ! actors.toSeq

    case MasterActor.MasterClose(host) =>
      actors.filter(_.path.toString.contains(host)).foreach(_ ! XVShutdown)

    case Terminated(terminatedActor) =>
      context.unwatch(terminatedActor)
      actors -= terminatedActor
  }
}


object MasterActor {
  val props = Props[MasterActor]

  @SerialVersionUID(1L) final case object MasterList
  @SerialVersionUID(1L) final case class MasterClose(host: String)
}