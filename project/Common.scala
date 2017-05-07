import sbt._
import sbt.Keys._
import sbtassembly.AssemblyKeys._
import sbtassembly.{MergeStrategy, PathList}

object Common {

  lazy val commonSettings = inThisBuild(
    Seq(
      organization := "io.kigi.dairaga",
      version := "0.0.1-SNAPSHOT",
      scalaVersion := "2.12.2")
  ) ++ Seq(
    parallelExecution in test := false,
    compileOrder in Compile := CompileOrder.JavaThenScala,
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