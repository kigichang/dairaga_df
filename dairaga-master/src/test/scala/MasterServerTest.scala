import akka.actor.AddressFromURIString
import com.google.inject.Guice
import com.typesafe.config.ConfigFactory
import dairaga.common.SQLUtils
import dairaga.data.AkkaSeeds
import dairaga.master.MasterNode
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

/**
  * Created by kigi on 01/06/2017.
  */
class MasterServerTest extends FlatSpec with Matchers with BeforeAndAfterAll {


  val injector = Guice.createInjector(new MasterModuleTest())

  val master = new MasterNode(injector.getInstance(classOf[AkkaSeeds]))

  val url = ConfigFactory.load().getString("dairaga.data.mariadb.url")

  override def beforeAll(): Unit = {
    SQLUtils.fastUpdate(url, "delete from seeds")
    master.run()
  }

  "master node" should "have port 15001 and auto-detect ip" in {
    master.cluster.selfAddress.port.nonEmpty should === (true)
    master.cluster.selfAddress.port.get should === (15001)
    master.cluster.selfAddress.host.get should not be ("127.0.0.1")
  }

  "master node" should "register itself as seed" in {
    val addr = SQLUtils.fastQuery(url, s"select address from seeds where address = '${master.cluster.selfAddress.toString}' and status = 1")

    addr.nonEmpty should === (true)
    AddressFromURIString(addr.get) should === (master.cluster.selfAddress)
  }

  override def afterAll(): Unit = {
    master.shutdown()
  }

}
