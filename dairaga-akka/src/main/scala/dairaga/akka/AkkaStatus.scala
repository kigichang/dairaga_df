package dairaga.akka

import akka.actor.Address
import scala.collection.immutable

/**
  * Created by kigi on 09/05/2017.
  */
trait AkkaStatus {

  def read:immutable.Seq[Address]

  def add(address: Address)

  def leave(address: Address)
}
