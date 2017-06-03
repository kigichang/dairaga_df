package dairaga.master

import com.google.inject.AbstractModule
import dairaga.data.AkkaSeeds

/**
  *
  * Created by kigi on 01/06/2017.
  */
class MasterModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[AkkaSeeds]).to(classOf[dairaga.gcp.AkkaSeedsImpl])
  }
}
