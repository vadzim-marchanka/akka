name := "akka"

version := "0.1"

scalaVersion := "2.12.8"

lazy val akkaVersion = "2.5.21"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence" % "2.5.21",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8"
)
