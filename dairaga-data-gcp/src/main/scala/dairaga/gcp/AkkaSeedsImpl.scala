package dairaga.gcp

import akka.actor.Address
import dairaga.data.AkkaSeeds

import scala.collection.immutable

/**
  * Created by kigi on 01/06/2017.
  */
class AkkaSeedsImpl extends AkkaSeeds {
  override def seeds: immutable.Seq[Address] = ???

  override def write(address: Address): Unit = ???

  override def remove(address: Address): Unit = ???
}
