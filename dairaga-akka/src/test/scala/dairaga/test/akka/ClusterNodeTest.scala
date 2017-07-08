package dairaga.test.akka

import akka.actor.{Actor, ActorContext, ActorRef, Address, Inbox, PoisonPill, Props}
import akka.cluster.pubsub.DistributedPubSubMediator.Publish
import akka.testkit.{ImplicitSender, TestKit}
import dairaga.akka.{ClusterNode, DairagaActor, XVShutdown}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.collection.immutable

/**
  * Created by kigi on 09/05/2017.
  */

object TestNode extends ClusterNode {
  override def seeds: immutable.Seq[Address] = immutable.Seq.empty[Address]

  var actor: ActorRef = null

  override def internPreStart(context: ActorContext): Unit = {
    super.internPreStart(context)
    actor = context.actorOf(Props(new DairagaActor {
      override def receive: Receive = {
        case msg: String => sender() ! msg

        //case XVShutdown =>
        //  mediator ! Publish(dairaga.env.XVClusterInfo, XVShutdown)
      }
    }))

    context.watch(actor)
  }

  var callInternPostStop: Boolean = false
  override def internPostStop(context: ActorContext): Unit = {
    callInternPostStop = true
    super.internPostStop(context)
  }

  var callChildOnTerminated: Boolean = false
  override def childOnTerminated(context: ActorContext, actor: ActorRef): Unit = {
    super.childOnTerminated(context, actor)
    callChildOnTerminated = true
  }

  var callPreStart: Boolean = false
  override def preStart(): Unit = {
    super.preStart()
    callPreStart = true
  }

  var callAfterRun: Boolean = false
  override def afterRun(): Unit = {
    super.afterRun()
    callAfterRun = true
  }

  var callPostStop: Boolean = false
  override def postStop(): Unit = {
    callPostStop = true
    super.postStop()
  }

  run()
}

class ClusterNodeTest extends TestKit(TestNode.system) with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {

  override def beforeAll(): Unit = {
    //TestNode.run()
  }

  "a simple node" must {
    "have correct status" in {

      awaitCond(TestNode.actor != null)

      TestNode.initialized should === (false)
      TestNode.started should === (true)
      TestNode.terminated === (false)

      TestNode.callPreStart should === (true)
      TestNode.callAfterRun should === (true)
    }

    "echo msg" in {
      val msg = "hello"

      TestNode.actor ! msg

      expectMsg(msg)
    }

    "terminate a child actor" in {
      TestNode.actor ! PoisonPill
      awaitCond(TestNode.callChildOnTerminated == true)
    }

    "be terminated when got XVShutdown" in {
     TestNode.intern ! XVShutdown

      awaitCond(TestNode.terminated == true)
      TestNode.callInternPostStop should === (true)
      TestNode.callPostStop should === (true)
    }
  }

  override def afterAll(): Unit = {
    //node.shutdown()
    TestNode.shutdown()
  }

}
