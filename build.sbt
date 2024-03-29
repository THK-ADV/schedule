val playSlickVersion = "5.1.0"
val scalaTestVersion = "3.2.15"

lazy val `schedule` = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := "schedule",
    maintainer := "Alexander Dobrynin <alexander.dobrynin@th-koeln.de>",
    version := "1.0",
    scalaVersion := "2.13.8",
    libraryDependencies ++= play,
    libraryDependencies ++= database,
    libraryDependencies ++= date,
    libraryDependencies ++= test,
    libraryDependencies ++= keycloak,
    libraryDependencies += filenameMacro,
    resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/",
    externalResolvers ++= Seq(
      "GitHub <THK-ADV> filename" at "https://maven.pkg.github.com/THK-ADV/filename",
      "GitHub <THK-ADV> keycloak-validation" at "https://maven.pkg.github.com/THK-ADV/keycloak-validation"
    ),
    credentials += Credentials(
      "GitHub Package Registry",
      "maven.pkg.github.com",
      "THK-ADV",
      System.getenv("GITHUB_TOKEN")
    )
  )

lazy val play = Seq(
  specs2 % Test,
  guice,
  ws,
  "com.typesafe.play" %% "play-json" % "2.9.4"
)

lazy val database = Seq(
  "com.typesafe.play" %% "play-slick" % playSlickVersion,
  "com.typesafe.play" %% "play-slick-evolutions" % playSlickVersion,
  "org.postgresql" % "postgresql" % "42.5.3"
)

lazy val date = Seq(
  "joda-time" % "joda-time" % "2.12.2"
)

lazy val test = Seq(
  "org.scalactic" %% "scalactic" % scalaTestVersion,
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
  "org.scalatest" %% "scalatest-wordspec" % scalaTestVersion % "test",
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % "test"
)

lazy val keycloak = Seq(
  "de.th-koeln.inf.adv" %% "keycloak-validation" % "0.1",
  "org.jboss.logging" % "jboss-logging" % "3.3.0.Final",
  "org.apache.httpcomponents" % "httpclient" % "4.5.14"
)

lazy val filenameMacro = "de.th-koeln.inf.adv" %% "filename" % "0.1"
