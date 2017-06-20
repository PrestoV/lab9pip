name := """play-java"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.11"

libraryDependencies += javaJdbc
libraryDependencies += cache
libraryDependencies += javaWs
libraryDependencies += javaJdbc
libraryDependencies += javaJpa
libraryDependencies += "org.hibernate" % "hibernate-core" % "4.3.11.Final"
libraryDependencies += "org.postgresql" % "postgresql" % "9.3-1102-jdbc41"
libraryDependencies ++= Seq(
  javaJpa,
  "org.hibernate" % "hibernate-entitymanager" % "4.3.11.Final"
)
PlayKeys.externalizeResources := false
libraryDependencies ++=  {
  val akkaV = "2.4.1"
  val activemqV = "5.14.5"
  Seq(
    "com.typesafe.akka" %% "akka-actor"       % akkaV,
    "com.typesafe.akka" %% "akka-camel"       % akkaV,
    "io.spray"          %% "spray-json"       % "1.3.1",
    "org.apache.activemq" % "activemq-camel"  % activemqV
  )
}