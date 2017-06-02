package dairaga

import java.util.UUID

import play.api.libs.json.{Json, Writes}

/**
  * Created by kigi on 19/05/2017.
  */
package object msg {

  object FromTypes {
    val Web: Int = 0
    val iOS: Int = 1
    val Android: Int = 2
  }

  @SerialVersionUID(1L)
  case class Request(time: Long, ip: String, callback: String, id: String = java.util.UUID.randomUUID().toString)
  implicit val requestFormat = Json.format[Request]

  trait Base {
    val customerId: String
    val from: Int
    val request: Request
    val customerUserId: Option[String]
    val xvUserId: String
    final val created: Long = System.currentTimeMillis()
  }

  trait JsonSupport[T <: Base] { self: T =>
    def json(implicit writes: Writes[T]): String =
      s"""{"data":${Json.stringify(Json.toJson(self))},"created":${self.created}}"""

    //override def toString = json
  }
}
