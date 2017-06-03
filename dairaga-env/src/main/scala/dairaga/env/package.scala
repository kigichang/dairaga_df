package dairaga

import scala.concurrent.duration._

/**
  * Created by kigi on 08/05/2017.
  */
package object env {

  /**
    * Dairaga cluser name
    */
  val AkkaClusterName: String = "dairaga"

  /**
    * Default date string format
    */
  val DateFormat: String = "yyyy-MM-dd"

  /**
    * Default time string format (24-hour clock)
    */
  val TimeFormat: String = "HH:mm:ss"

  /**
    * Default datetime string format
    */
  val DateTimeFormat: String = s"$DateFormat $TimeFormat"

  /**
    * Default character encoding (UTF8)
    */
  val Charset: String = "UTF-8"

  val XVTerminateWait: Duration = 60 seconds

  val InternActor = "xv-intern"
  val ClusterInfo = "DairagaXVInfo"

  /**
    * Default Dairaga HTTP Server PORT
    */
  val HttpPort = 15080
}
