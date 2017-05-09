import Dependencies._

name := """dairaga"""

lazy val root = project.in(file(".")).aggregate(env, common, config, core, akka)

lazy val env = project.in(file("dairaga-env"))
  .disablePlugins(AssemblyPlugin)
  .settings(Common.commonSettings)

lazy val common = project.in(file("dairaga-common"))
  .disablePlugins(AssemblyPlugin)
  .settings(
    Common.commonSettings,
    libraryDependencies ++= Seq(
      commonLang,
      scalaTest
    )
  ).dependsOn(env)

lazy val config = project.in(file("dairaga-config"))
  .disablePlugins(AssemblyPlugin)
  .settings(
    Common.commonSettings,
    libraryDependencies ++= Seq(
      typesafeConfig,
      scalaTest
    )
  ).dependsOn(env)

lazy val core = project.in(file("dairaga-core"))
  .disablePlugins(AssemblyPlugin)
  .settings(
    Common.commonSettings,
    libraryDependencies ++= Seq(
      guice,
      logback,
      scalaTest
    )
  ).dependsOn(env, common, config)

lazy val akka = project.in(file("dairaga-akka"))
  .disablePlugins(AssemblyPlugin)
  .settings(
    Common.commonSettings,
    libraryDependencies ++= akkaHttp ++ akkaCluster ++ Seq(
      logback,
      scalaTest
    )
  ).dependsOn(common, config, env)