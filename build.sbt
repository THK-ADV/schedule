resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"

val playSlickVersion = "5.0.0"
val scalaTestVersion = "3.2.7"

lazy val `schedule` = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := "schedule",
    maintainer := "Alexander Dobrynin <alexander.dobrynin@th-koeln.de>",
    version := "1.0",
    scalaVersion := "2.13.6",
    libraryDependencies ++= play,
    libraryDependencies ++= database,
    libraryDependencies ++= date,
    libraryDependencies ++= test,
    libraryDependencies ++= keycloak,
    libraryDependencies += filenameMacro
  )

lazy val play = Seq(
  specs2 % Test,
  guice,
  ws,
  "com.typesafe.play" %% "play-json" % "2.9.2"
)

lazy val database = Seq(
  "com.typesafe.play" %% "play-slick" % playSlickVersion,
  "com.typesafe.play" %% "play-slick-evolutions" % playSlickVersion,
  "org.postgresql" % "postgresql" % "42.2.19"
)

lazy val date = Seq(
  "joda-time" % "joda-time" % "2.10.10"
)

lazy val test = Seq(
  "org.scalactic" %% "scalactic" % scalaTestVersion,
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
  "org.scalatest" %% "scalatest-wordspec" % scalaTestVersion % "test"
)

lazy val keycloak = Seq(
  "de.th-koeln.inf.adv" %% "keycloak-validation" % "0.1",
  "org.jboss.logging" % "jboss-logging" % "3.3.0.Final",
  "org.apache.httpcomponents" % "httpclient" % "4.5.1"
)

lazy val filenameMacro = "de.th-koeln.inf.adv" %% "filename" % "0.1"
