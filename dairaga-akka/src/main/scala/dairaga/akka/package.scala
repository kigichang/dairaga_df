package dairaga


/**
  * Created by kigi on 5/9/17.
  */
package object akka {

  trait XVMessage

  @SerialVersionUID(1L) final case object XVShutdown extends XVMessage

  @SerialVersionUID(1L) final case object XVPing extends XVMessage

  @SerialVersionUID(1L) final case object XVRegister extends XVMessage

  @SerialVersionUID(1L) final case object XVHeartBeat extends XVMessage

}
