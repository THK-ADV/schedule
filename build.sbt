val playSlickVersion = "6.1.1"
val scalaTestVersion = "3.2.19"
val guiceVersion = "5.1.0"
val keycloakVersion = "24.0.3"

lazy val `schedule` = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := "schedule",
    maintainer := "Alexander Dobrynin <alexander.dobrynin@th-koeln.de>",
    version := "1.0",
    scalaVersion := "3.3.3",
    libraryDependencies ++= play,
    libraryDependencies ++= database,
    libraryDependencies ++= test,
    libraryDependencies ++= keycloak,
    libraryDependencies += kafka,
    resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/",
    externalResolvers ++= Seq(
      "GitHub <THK-ADV> filename" at "https://maven.pkg.github.com/THK-ADV/filename",
      "GitHub <THK-ADV> keycloak-validation" at "https://maven.pkg.github.com/THK-ADV/keycloak-validation"
    ),
    credentials += Credentials(
      "GitHub Package Registry",
      "maven.pkg.github.com",
      "THK-ADV",
      sys.env.getOrElse("GITHUB_TOKEN", "")
    ),
    (Universal / javaOptions) ++= Seq(
      "-Dpidfile.path=/dev/null"
    )
  )

lazy val play = Seq(
  specs2 % Test,
  ehcache,
  ws,
  guice,
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.17.1",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.17.1",
)

lazy val database = Seq(
  "org.playframework" %% "play-slick" % playSlickVersion,
  "org.playframework" %% "play-slick-evolutions" % playSlickVersion,
  "org.postgresql" % "postgresql" % "42.7.3"
)

lazy val test = Seq(
  "org.scalactic" %% "scalactic" % scalaTestVersion,
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
  "org.scalatest" %% "scalatest-wordspec" % scalaTestVersion % "test",
  "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % "test"
)

val keycloak = Seq(
  "org.keycloak" % "keycloak-core" % keycloakVersion,
  "org.keycloak" % "keycloak-adapter-core" % keycloakVersion,
  "org.jboss.logging" % "jboss-logging" % "3.5.3.Final"
)

lazy val kafka = "org.apache.kafka" % "kafka-clients" % "3.8.0"
