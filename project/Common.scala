import sbt._
import sbt.Keys._
import sbtassembly.AssemblyKeys._
import sbtassembly.{MergeStrategy, PathList}

object Common {

  lazy val commonSettings = Seq(
    organization := "me.dataforce.dairaga",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.2",
    parallelExecution in test := false,
    compileOrder in Compile := CompileOrder.Mixed, // change to Mixed for Play
    fork := true
  )


  lazy val assemblySettings = Seq(
    assemblyMergeStrategy in assembly := {
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)},
    test in assembly := {}
  )
}