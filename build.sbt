import Dependencies._

name := """dairaga"""

lazy val root = project.in(file(".")).aggregate(env, common, config, core, msg, akka, master, collector, dashboard)

lazy val env = project.in(file("dairaga-env"))
  .disablePlugins(AssemblyPlugin, PlayScala)
  .settings(Common.commonSettings)

lazy val common = project.in(file("dairaga-common"))
  .disablePlugins(AssemblyPlugin, PlayScala)
  .settings(
    Common.commonSettings,
    libraryDependencies ++= Seq(
      commonLang,
      scalaTest
    )
  ).dependsOn(env)

lazy val config = project.in(file("dairaga-config"))
  .disablePlugins(AssemblyPlugin, PlayScala)
  .settings(
    Common.commonSettings,
    libraryDependencies ++= Seq(
      typesafeConfig,
      scalaTest
    )
  ).dependsOn(env)

lazy val msg = project.in(file("dairaga-msg"))
  .disablePlugins(AssemblyPlugin, PlayScala)
  .settings(
    Common.commonSettings,
    libraryDependencies ++= Seq(
      playJson,
      commonLang,
      scalaTest
    )
  )

lazy val core = project.in(file("dairaga-core"))
  .disablePlugins(AssemblyPlugin, PlayScala)
  .settings(
    Common.commonSettings,
    libraryDependencies ++= Seq(
      Dependencies.guice,
      logback,
      scalaTest
    )
  ).dependsOn(env, common, config)

lazy val akka = project.in(file("dairaga-akka"))
  .disablePlugins(AssemblyPlugin, PlayScala)
  .settings(
    Common.commonSettings,
    libraryDependencies ++= akkaHttp ++ akkaCluster ++ Seq(
      logback,
      scalaTest,
      playJson % Test
    )
  ).dependsOn(common, config, env)

lazy val master = project.in(file("dairaga-master"))
  .enablePlugins(AssemblyPlugin)
  .disablePlugins(PlayScala)
  .settings(
    Common.commonSettings,
    Common.assemblySettings,
    mainClass in assembly := Some("dairaga.master.MasterServer"),
    libraryDependencies ++= Seq(
      logback,
      playJson,
      scalaTest
    )
  ).dependsOn(akka)

lazy val collector = project.in(file("dairaga-collector"))
  .enablePlugins(AssemblyPlugin).disablePlugins(PlayScala)
  .settings(
    Common.commonSettings,
    Common.assemblySettings,
    libraryDependencies ++= Seq(
      logback,
      playJson,
      scalaTest
    )
  ).dependsOn(akka)

lazy val dashboard = project.in(file("dairaga-dashboard"))
  .enablePlugins(PlayScala)
  .disablePlugins(AssemblyPlugin)
  .settings(
    resolvers += Resolver.sonatypeRepo("snapshots"),
    Common.commonSettings,
    libraryDependencies ++= Seq(
      play.sbt.Play.autoImport.guice,
      filters,
      "org.scalatestplus.play" %% "scalatestplus-play" % "3.0.0-M3" % Test
    )
  )
