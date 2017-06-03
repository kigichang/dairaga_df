package dairaga.data

import akka.actor.Address

import scala.collection.immutable

/**
  * Akka seeds management
  *
  * Dairaga nodes find seeds or register itself as seed with this.
  *
  * Created by kigi on 31/05/2017.
  */
trait AkkaSeeds {

  /**
    * read current seeds
    * @return
    */
  def seeds:immutable.Seq[Address]

  /**
    * write a node as seed
    * @param address
    */
  def write(address: Address): Unit

  /**
    * remove an old seed
    * @param address
    */
  def remove(address: Address): Unit

}
