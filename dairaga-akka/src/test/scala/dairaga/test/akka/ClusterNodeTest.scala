package dairaga.test.akka

import akka.actor.{Actor, ActorContext, ActorRef, Address, Inbox, Props}
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

        case XVShutdown =>
          mediator ! Publish(dairaga.env.XVClusterInfo, XVShutdown)
      }
    }))

    context.watch(actor)
  }

  run()
}

class ClusterNodeTest extends TestKit(TestNode.system) with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {

  override def beforeAll(): Unit = {
    //TestNode.run()
  }

  "a simple node" must {
    "have correct status" in {
      TestNode.initialized should === (false)
      TestNode.started should === (true)
      TestNode.terminated === (false)
    }

    "echo msg" in {
      val msg = "hello"

      awaitCond(TestNode.actor != null)

      TestNode.actor ! msg

      expectMsg(msg)
    }

    "be terminated when got XVShutdown" in {
     TestNode.actor ! XVShutdown

      awaitCond(TestNode.terminated == true)
    }
  }

  override def afterAll(): Unit = {
    //node.shutdown()
    TestNode.shutdown()
  }

}
