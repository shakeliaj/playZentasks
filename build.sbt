name := """zenTasks"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava,PlayEbean,PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaCore, javaJdbc,
  cache,
  javaWs
, evolutions,"org.yaml" % "snakeyaml" % "1.17")


routesGenerator := InjectedRoutesGenerator
includeFilter in (Assets, LessKeys.less) := "main.less"|"login.less"
