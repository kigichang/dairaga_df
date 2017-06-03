package dairaga

/**
  * Dairaga framework configuration key name
  *
  * Created by kigi on 08/05/2017.
  */
package object key {

  /**
    * Auto-detecting host ip via network interface name.
    *
    * Boolean
    */
  val XVNetworkAutoDetect = "dairaga.network.auto-detect"

  /**
    * The network interface name for auto-detecting host ip
    *
    * @see XVNetworkAutoDetect
    *
    * Array of String
    */
  val XVNetworkInterfaces = "dairaga.network.interfaces"

  /**
    * The node host ip
    *
    * String
    */
  val XVServerIp = "dairaga.server.ip"

  /**
    * The node port
    *
    * Int
    */
  val XVServerPort = "dairaga.server.port"

  /**
    * The http server host ip
    *
    * String
    */
  val XVHttpIp = "dairaga.http.ip"

  /**
    * The http port
    *
    * Int
    */
  val XVHttpPort = "dairaga.http.port"
}
