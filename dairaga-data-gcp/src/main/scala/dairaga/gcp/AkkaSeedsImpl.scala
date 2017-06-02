package dairaga.gcp

import akka.actor.Address
import dairaga.data.AkkaSeeds

import scala.collection.immutable

/**
  * Created by kigi on 01/06/2017.
  */
class AkkaSeedsImpl extends AkkaSeeds {

  private var _seeds = immutable.Seq.empty[Address]

  override def seeds: immutable.Seq[Address] = _seeds

  override def write(address: Address): Unit = {
    _seeds = _seeds :+ address
  }

  override def remove(address: Address): Unit = {
    _seeds = _seeds.filterNot( _ == address)
  }
}
