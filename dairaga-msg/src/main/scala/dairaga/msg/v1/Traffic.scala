package dairaga.msg.v1

import dairaga.msg.{Base, FromTypes, JsonSupport, Request}
import play.api.libs.json.Json

/**
  * Created by kigi on 19/05/2017.
  */
class Traffic {
  case class WebTraffic(override val customerId: String,
                        override val customerUserId: Option[String],
                        override val xvUserId: String,
                        override val request: Request,
                        url: String,
                        referer: String,
                        agent: String) extends Base with JsonSupport[WebTraffic] {

    override val from: Int = FromTypes.Web

  }

  implicit val webTrafficFormat = Json.format[WebTraffic]

  case class AppTraffic(override val customerId: String,
                        override val from: Int,
                        override val request: Request,
                        override val customerUserId: Option[String],
                        override val xvUserId: String,
                        screen: String,
                        os: String,
                        app: String) extends Base with JsonSupport[AppTraffic] {
  }

  implicit val appTrafficFormat = Json.format[AppTraffic]

}
