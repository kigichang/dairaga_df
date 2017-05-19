package dairaga.msg.v1

import dairaga.msg.{Base, JsonSupport, Request}
import play.api.libs.json.Json
import scala.collection.immutable

import org.apache.commons.lang3.ArrayUtils

/**
  * Created by kigi on 19/05/2017.
  */
object Events {
  case class Event(override val customerId: String,
                   override val from: Int,
                   override val request: Request,
                   override val customerUserId: Option[String],
                   override val xvUserId: String,
                  category: String,
                  action: String,
                  targets: Array[String] = ArrayUtils.EMPTY_STRING_ARRAY,
                  value: Long = 0L,
                  extra: immutable.Map[String, String] = immutable.Map.empty[String, String]) extends Base with JsonSupport[Event] {
  }

  implicit val eventFormat = Json.format[Event]
}