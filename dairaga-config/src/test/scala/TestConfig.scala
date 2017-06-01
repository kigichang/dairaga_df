import com.typesafe.config.ConfigFactory
import dairaga.config.DairagaConfig
import dairaga.key._
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by kigi on 08/05/2017.
  */
class TestConfig extends FlatSpec with Matchers {

  "Resolve config" should "override old value" in {
    val config = DairagaConfig.resolve(ConfigFactory.parseString(s"""$XVNetworkInterfaces=["test"]"""))

    val list = config.getStringList(XVNetworkInterfaces)
    list.size() should === (1)
    list.get(0) should === ("test")
    config.getBoolean(XVNetworkAutoDetect) should === (true)
  }

  "Load specific configuration" should "contains default value" in {
    val config = DairagaConfig.load("test-config")

    config.getBoolean("test") should === (true)
    config.getBoolean(XVNetworkAutoDetect) should === (true)
  }

  /*"Load default application config" should "override default value" in {
    val config = DairagaConfig.load
    val list = config.getStringList(XVNetworkInterfaces)
    list.size() should === (1)
    list.get(0) should === ("test")
    config.getBoolean(XVNetworkAutoDetect) should === (false)
  }*/


}
