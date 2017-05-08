package dairaga.config

import com.typesafe.config.{Config, ConfigFactory}

/**
  * Created by kigi on 08/05/2017.
  */
object DairagaConfig {

  val intern: Config = ConfigFactory.load("dairaga-config")

  def resolve(custom: Config, base: Config): Config = custom.withFallback(base).resolve()

  def resolve(config: Config): Config = resolve(config, intern)

  def load = resolve(ConfigFactory.load())

  def load(resourceBaseName: String) = resolve(ConfigFactory.load(resourceBaseName))
}
