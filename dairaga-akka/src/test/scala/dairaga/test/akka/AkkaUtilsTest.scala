package dairaga.test.akka

import akka.actor.Address
import dairaga.akka.AkkaUtils
import dairaga.env._
import dairaga.key._
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.immutable
import scala.util.Try

/**
  * Created by kigi on 5/8/17.
  */
class AkkaUtilsTest extends FlatSpec with Matchers {

  "AkkaUtils.cluster" should "initialize a one node cluster" in {
    val tmp = Try {
      AkkaUtils.cluster(immutable.Seq.empty[Address])
    }

    tmp.isSuccess should === (true)

    tmp.get.settings.systemName should === (AkkaClusterName)
    AkkaUtils.shutdown(tmp.get)
  }

  "AkkaUtils.cluster" should "read ip from configuration" in {
    val tmp = Try {
      AkkaUtils.cluster(immutable.Seq.empty[Address], "test-ip")
    }

    tmp.isSuccess should === (true)
    val cluster = tmp.get
    println(cluster.settings.config.getStringList(XVNetworkInterfaces))
    cluster.selfAddress.host.get shouldNot be ("127.0.0.1")
    AkkaUtils.shutdown(tmp.get)
  }
}
