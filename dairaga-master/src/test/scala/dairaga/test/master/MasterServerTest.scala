package dairaga.test.master

import akka.actor.{ActorRef, Address, AddressFromURIString}
import akka.pattern.ask
import akka.testkit.{ImplicitSender, TestKit}
import akka.util.Timeout
import com.google.inject.Guice
import com.typesafe.config.ConfigFactory
import dairaga.akka.{ClusterNode, XVHeartBeat, XVShutdown}
import dairaga.common.SQLUtils
import dairaga.data.AkkaSeeds
import dairaga.master.{MasterActor, MasterNode}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.collection.immutable
import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by kigi on 01/06/2017.
  */

object MasterServerTest {
  val injector = Guice.createInjector(new MasterModuleTest())

  val masterNode = new MasterNode(injector.getInstance(classOf[AkkaSeeds]))

  val url = ConfigFactory.load().getString("dairaga.data.mariadb.url")

  masterNode.run()
}

class OtherNode extends ClusterNode {
  override def seeds: immutable.Seq[Address] = immutable.Seq(MasterServerTest.masterNode.cluster.selfAddress)
}

class MasterServerTest extends TestKit(MasterServerTest.masterNode.system) with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {

  import MasterServerTest._

  val other1 = new OtherNode()
  val other2 = new OtherNode()

  override def beforeAll(): Unit = {
    other1.run("other1")
    other2.run("other2")
  }

  "master node" should {
    "have port 15001 and auto-detect ip" in {
      masterNode.cluster.selfAddress.port.nonEmpty should === (true)
      masterNode.cluster.selfAddress.port.get should === (15001)
      masterNode.cluster.selfAddress.host.get should not be ("127.0.0.1")
    }

    "register itself as seed" in {
      val addr = SQLUtils.fastQuery(url, s"select address from seeds where address = '${masterNode.cluster.selfAddress.toString}' and status = 1")

      addr.nonEmpty should === (true)
      AddressFromURIString(addr.get) should === (masterNode.cluster.selfAddress)
    }


    "ping other cluster node" in {
      expectNoMsg()  // wait a moment

      masterNode.master ! XVHeartBeat

      expectNoMsg()  // wait a moment

      implicit val timeout = Timeout(5 seconds)
      val tmp = Await.result((masterNode.master ? MasterActor.MasterList).mapTo[Seq[ActorRef]], 5 seconds)

      tmp.length should === (2)
    }

    "close some cluster node" in {
      masterNode.master ! MasterActor.MasterClose(s"${other1.cluster.selfAddress.host.get}:${other1.cluster.selfAddress.port.get}")

      awaitCond(other1.terminated == true, 10 seconds)
      other2.terminated should === (false)
    }

    "shutdown all cluster node exclude self" in {
      masterNode.master ! XVShutdown
      awaitCond(other2.terminated == true, 10 seconds)

      masterNode.terminated should === (false)
    }

  }

  override def afterAll(): Unit = {
    other1.shutdown()
    other2.shutdown()
    masterNode.shutdown()
  }

}
