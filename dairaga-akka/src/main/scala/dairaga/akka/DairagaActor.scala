package dairaga.akka

import akka.actor.SupervisorStrategy.{Escalate, Resume}
import akka.actor.{Actor, ActorLogging, ActorRef, OneForOneStrategy}
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.Put

import scala.concurrent.duration._
import scala.concurrent.ExecutionContextExecutor
import scala.util.control.NonFatal

/**
  * Created by kigi on 09/05/2017.
  */
trait DairagaActor extends Actor with ActorLogging {

  private implicit val executionContext: ExecutionContextExecutor = context.system.dispatcher

  protected val mediator: ActorRef = DistributedPubSub(context.system).mediator

  override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
    case NonFatal(_) =>
      Resume
    case _ =>
      Escalate
  }

  mediator ! Put(self)

}
