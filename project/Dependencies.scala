import sbt._

object Dependencies {

  val akkaVersion: String = "2.5.6"

  lazy val akka: Seq[ModuleID] = Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test
  )

  lazy val akkaCluster: Seq[ModuleID] = akka ++
    Seq(
      "com.typesafe.akka" %% "akka-slf4j",
      "com.typesafe.akka" %% "akka-cluster",
      "com.typesafe.akka" %% "akka-cluster-tools",
      "com.typesafe.akka" %% "akka-cluster-metrics").map(_ % akkaVersion)

  val akkaHttpVersion = "10.0.8"

  lazy val akkaHttp = Seq(
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % "test")

  lazy val scalaTest: ModuleID = "org.scalatest" %% "scalatest" % "3.0.3"

  lazy val logback: ModuleID = "ch.qos.logback" % "logback-classic" % "1.2.3"

  lazy val typesafeConfig: ModuleID = "com.typesafe" % "config" % "1.3.1"

  lazy val commonLang: ModuleID = "org.apache.commons" % "commons-lang3" % "3.7"

  lazy val guice: ModuleID = "com.google.inject" % "guice" % "4.1.0"

  lazy val playJson: ModuleID = "com.typesafe.play" %% "play-json" % "2.6.7"

  lazy val mariadb: ModuleID = "org.mariadb.jdbc" % "mariadb-java-client" % "2.2.0"

}
