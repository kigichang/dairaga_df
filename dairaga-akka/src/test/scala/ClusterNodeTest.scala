import akka.actor.{Actor, ActorRef, Address, Inbox, Props}
import dairaga.akka.ClusterNode
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.collection.immutable
import scala.concurrent.duration._

/**
  * Created by kigi on 09/05/2017.
  */
class ClusterNodeTest extends FlatSpec with Matchers with BeforeAndAfterAll {

  val node = new ClusterNode {
    override def seeds: immutable.Seq[Address] = immutable.Seq.empty[Address]

    var actor: ActorRef = null

    override def afterRun(): Unit = {
      actor = system.actorOf(Props(new Actor {
        override def receive: Receive = {
          case msg: String => sender() ! msg
        }
      }))
    }
  }


  override def beforeAll(): Unit = {
    node.run()
  }

  "a simple node" should "echo msg" in {
    val msg = "hello"

    val inbox = Inbox.create(node.system)

    node.actor.tell(msg, inbox.getRef())

    inbox.receive(5 seconds) should === (msg)
  }

  override def afterAll(): Unit = {
    node.shutdown()
  }

}
