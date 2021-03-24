resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"

val playSlickVersion = "5.0.0"

lazy val `schedule` = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := "schedule",
    maintainer := "Alexander Dobrynin <alexander.dobrynin@th-koeln.de>",
    version := "1.0",
    scalaVersion := "2.13.5",
    libraryDependencies ++= playDependencies,
    libraryDependencies ++= databaseDependendies
  )

lazy val playDependencies = Seq(
  specs2 % Test,
  guice,
  "com.typesafe.play" %% "play-json" % "2.9.2"
)

lazy val databaseDependendies = Seq(
  "com.typesafe.play" %% "play-slick" % playSlickVersion,
  "com.typesafe.play" %% "play-slick-evolutions" % playSlickVersion,
  "org.postgresql" % "postgresql" % "42.2.19"
)
