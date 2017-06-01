import akka.actor.{Address, AddressFromURIString}
import com.typesafe.config.ConfigFactory
import dairaga.common.SQLUtils
import dairaga.mariadb.AkkaSeedsImpl
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

/**
  * Created by kigi on 31/05/2017.
  */
class AkkaSeedsTest extends FlatSpec with Matchers with BeforeAndAfterAll {

  val address = Address("akka", "test", "127.0.0.1", 2552)

  val addressString = address.toString

  val akkaSeeds = new AkkaSeedsImpl

  val url = ConfigFactory.load().getString("dairaga.data.mariadb.url")

  def findAddress(addr: String): Option[Address] = {
    val tmp = SQLUtils.fastQuery(url, s"select address from seeds where address = '${addr}' and status = 1")

    tmp.map(AddressFromURIString(_))
  }

  override def beforeAll(): Unit = {
    SQLUtils.fastUpdate(url, "delete from seeds")
  }

  "AkkaSeeds" should "write address" in {
    akkaSeeds.write(address)

    val addr = findAddress(addressString)

    addr.nonEmpty should === (true)

    address should === (addr.get)
  }

  "AkkaSeeds" should "get seq of address" in {
    val seeds = akkaSeeds.seeds

    seeds.length should === (1)

    seeds(0) should === (address)
  }

  "AkkaSeeds" should "remove address" in {
    akkaSeeds.remove(address)

    val tmp = findAddress(addressString)

    tmp.nonEmpty should === (false)
  }

}
