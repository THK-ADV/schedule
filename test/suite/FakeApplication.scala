package suite

import org.apache.pekko.stream.Materializer
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Configuration
import play.api.inject.guice.{GuiceApplicationBuilder, GuiceableModule}

trait FakeApplication {
  self: GuiceOneAppPerSuite =>

  val fakeDbConfig = Configuration(
    "slick.dbs.default.db.url" -> "jdbc:postgresql://localhost:5432/postgres",
    "slick.dbs.default.db.user" -> "postgres",
    "slick.dbs.default.db.databaseName" -> "postgres",
    "slick.dbs.default.db.password" -> "",
    "play.evolutions.db.default.autoApply" -> "false",
    "play.evolutions.db.default.enabled" -> "false"
  )

  implicit lazy val materializer: Materializer = app.materializer

  // import play.api.inject.bind
  protected def bindings: Seq[GuiceableModule] = Seq.empty

  override def fakeApplication() = new GuiceApplicationBuilder()
    .configure(fakeDbConfig)
    .overrides(bindings*)
    .build()
}
