package dairaga

import scala.concurrent.duration._

/**
  * Created by kigi on 08/05/2017.
  */
package object env {

  val AkkaClusterName: String = "dairaga"

  val DateFormat: String = "yyyy-MM-dd"
  val TimeFormat: String = "HH:mm:ss"
  val DateTimeFormat: String = s"$DateFormat $TimeFormat"

  val Charset: String = "UTF-8"

  val TerminateWait: Duration = 60 seconds

}
