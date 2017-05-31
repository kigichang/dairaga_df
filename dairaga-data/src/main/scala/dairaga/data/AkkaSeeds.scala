package dairaga.data

import akka.actor.Address

import scala.collection.immutable

/**
  * Created by kigi on 31/05/2017.
  */
trait AkkaSeeds {

  def seeds:immutable.Seq[Address]

  def write(address: Address): Unit

  def remove(address: Address): Unit

}
