name := """ingestion-util"""

organization := "com.harrys"

version := "0.2.0"

scalaVersion := "2.11.7"

exportJars := true

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "commons-io" % "commons-io" % "2.4",
  "ma.glasnost.orika" % "orika-core" % "1.4.6",
  "org.apache.httpcomponents" % "httpclient" % "4.4.1",
  "org.json4s" %% "json4s-core" % "3.2.11",
  "org.json4s" %% "json4s-jackson" % "3.2.11",
  "org.json4s" %% "json4s-ext" % "3.2.11",
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41"
)