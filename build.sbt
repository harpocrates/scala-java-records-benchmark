ThisBuild / scalaVersion     := "2.12.14-bin-indy1-SNAPSHOT"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "records"
// ThisBuild / javaOptions      += "-XX:CompileCommand=print,records.LargeScala::*"
// Compile / run / fork        := true


lazy val scalaReflect = Def.setting { "org.scala-lang" % "scala-reflect" % scalaVersion.value }


compileOrder := CompileOrder.JavaThenScala

lazy val root = (project in file("."))
  .dependsOn(macroSub)
  .settings(
    name := "Record Benchmarks" // ,
  //  scalacOptions += "-Xprint:parser,typer,patmat,erasure,posterasure,cleanup,jvm"
  )
  .enablePlugins(JmhPlugin)

lazy val macroSub = (project in file("macro"))
  .settings(
    libraryDependencies += scalaReflect.value
    // other settings here
  )
// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
