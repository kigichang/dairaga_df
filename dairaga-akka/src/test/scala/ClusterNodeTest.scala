import akka.actor.{Actor, ActorRef, Address, Inbox, Props}
import akka.testkit.{ImplicitSender, TestKit}
import dairaga.akka.{ClusterNode, XVShutdown}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.collection.immutable
import scala.concurrent.duration._

/**
  * Created by kigi on 09/05/2017.
  */


object TestNode extends ClusterNode {
  override def seeds: immutable.Seq[Address] = immutable.Seq.empty[Address]

  var actor: ActorRef = null

  var terminated: Boolean = false

  override def afterRun(): Unit = {
    actor = system.actorOf(Props(new Actor {
      override def receive: Receive = {
        case msg: String => sender() ! msg
      }
    }))
    terminated = false
  }

  override def postStop(): Unit = {
    terminated = true
    super.postStop()
  }

  run()
}

class ClusterNodeTest extends TestKit(TestNode.system) with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {

  override def beforeAll(): Unit = {
    //TestNode.run()
  }

  "a simple node" should {
    "echo msg" in {
      val msg = "hello"

      val inbox = Inbox.create(TestNode.system)

      TestNode.actor.tell(msg, inbox.getRef())

      inbox.receive(5 seconds) should === (msg)
    }

    "be terminated when got XVShutdown" in {
      TestNode.inter ! XVShutdown
      awaitCond(TestNode.terminated == true, 10 seconds)
    }
  }

  override def afterAll(): Unit = {
    //node.shutdown()
  }

}
