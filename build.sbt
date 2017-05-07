import Dependencies._

name := """dairaga"""

lazy val root = project.in(file(".")).aggregate(common, config, core)

lazy val common = project.in(file("dairaga-common"))
  .disablePlugins(AssemblyPlugin)

lazy val config = project.in(file("dairaga-config"))
  .disablePlugins(AssemblyPlugin)
  .settings(
    Common.commonSettings,
    libraryDependencies ++= Seq(
      typesafeConfig,
      scalaTest
    )
  )

lazy val core = project.in(file("dairaga-core"))
  .disablePlugins(AssemblyPlugin)
  .settings(
    Common.commonSettings,
    libraryDependencies ++= akkaHttp ++ akkaCluster ++ Seq(
      logback,
      scalaTest
    )
  ).dependsOn(common, config)