import sbt.Keys.{libraryDependencies, mainClass}

name := "akka"
version := "0.1.0"
organization in ThisBuild := "com.marchanka"
scalaVersion in ThisBuild := "2.12.8"


lazy val dependencies =
  new {
    val akkaVersion = "2.5.21"

    val actor = "com.typesafe.akka" %% "akka-actor" % akkaVersion
    val testKit = "com.typesafe.akka" %% "akka-testkit" % akkaVersion
    val persistence = "com.typesafe.akka" %% "akka-persistence" % akkaVersion
    val remote = "com.typesafe.akka" %% "akka-remote" % akkaVersion
    val cluster = "com.typesafe.akka" %% "akka-cluster" % akkaVersion
    val metrics = "com.typesafe.akka" %% "akka-cluster-metrics" % akkaVersion
    val tools = "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion
    val scalatest = "org.scalatest" %% "scalatest" % "3.0.5" % "test"
    val leveldb = "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8"
}

lazy val commonDependencies = Seq(
  dependencies.actor,
  dependencies.testKit,
  dependencies.persistence,
  dependencies.remote,
  dependencies.cluster,
  dependencies.metrics,
  dependencies.tools,
  dependencies.scalatest,
  dependencies.leveldb,
)

lazy val api = (project in file("api")).settings(
  name := "api",
  version := "0.1.0",
  libraryDependencies ++= commonDependencies
)

lazy val companies = (project in file("companies")).settings(
  name := "companies",
  libraryDependencies ++= commonDependencies,
  test in assembly := {},
  mainClass in assembly := Some("com.marchanka.akka.company.CompanyApplication"),
  assemblyJarName in assembly := "company.jar"
).dependsOn(api)

lazy val newspaper = (project in file("newspaper")).settings(
  name := "newspaper",
  libraryDependencies ++= commonDependencies,
  mainClass in assembly := Some("com.marchanka.akka.newspaper.NewspaperApplication"),
  assemblyJarName in assembly := "newspaper.jar"
).dependsOn(api)

