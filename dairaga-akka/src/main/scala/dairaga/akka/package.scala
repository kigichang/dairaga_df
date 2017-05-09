package dairaga

/**
  * Created by kigi on 5/9/17.
  */
package object akka {

  trait XVMessage

  @SerialVersionUID(1L) final object XVShutdown extends XVMessage

  @SerialVersionUID(1L) final object XVPing extends XVMessage

  @SerialVersionUID(1L) final object XVRegister extends XVMessage

}
