package dairaga


/**
  * Created by kigi on 5/9/17.
  */
package object akka {

  /**
    * Communication messages for Dairaga cluster nodes.
    */
  trait XVMessage

  /**
    * shutdown node
    */
  @SerialVersionUID(1L) final case object XVShutdown extends XVMessage

  /**
    * ping nodes
    */
  @SerialVersionUID(1L) final case object XVPing extends XVMessage

  /**
    * reply ping message
    */
  @SerialVersionUID(1L) final case object XVRegister extends XVMessage

  /**
    * node heartbeat message
    */
  @SerialVersionUID(1L) final case object XVHeartBeat extends XVMessage

}
