package dairaga.config

import com.typesafe.config.{Config, ConfigFactory}

/**
  * Dairaga Framework Configuration Manager
  *
  * It uses Typesafe Configuration.
  *
  * Created by kigi on 08/05/2017.
  */
object DairagaConfig {

  /**
    * load Dairaga Framework default settings
    */
  private [dairaga] val intern: Config = ConfigFactory.load("dairaga-config")

  /**
    * combine two configurations and resolve all values
    *
    * @param custom the new configuration
    * @param base   the original configuration
    * @return       combined configuration
    */
  def resolve(custom: Config, base: Config): Config = custom.withFallback(base).resolve()

  def resolve(config: Config): Config = resolve(config, intern)

  /**
    * Load default configuration
    *
    * @return
    */
  def load = resolve(ConfigFactory.load())

  /**
    * Load configuration with resource name
    *
    * @param resourceBaseName
    * @return
    */
  def load(resourceBaseName: String) = resolve(ConfigFactory.load(resourceBaseName))
}
