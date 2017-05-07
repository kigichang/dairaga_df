import sbt._
import sbt.Keys._

object Dependencies {

  val akkaVersion = "2.5.1"

  lazy val akkaCluster =
    Seq(
      "com.typesafe.akka" %% "akka-slf4j",
      "com.typesafe.akka" %% "akka-cluster",
      "com.typesafe.akka" %% "akka-cluster-tools",
      "com.typesafe.akka" %% "akka-cluster-metrics").map (_ % akkaVersion) :+
      ("com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test")

  val akkaHttpVersion = "10.0.6"

  lazy val akkaHttp = Seq(
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % "test")

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1"

  lazy val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"

  lazy val typesafeConfig = "com.typesafe" % "config" % "1.3.1"
}