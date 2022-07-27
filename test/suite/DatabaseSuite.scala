package suite

import database.tables.{
  FacultyTable,
  GraduationTable,
  StudyProgramTable,
  TeachingUnitTable
}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.db.slick.DatabaseConfigProvider

import java.sql.Timestamp
import java.util.UUID
import scala.concurrent.ExecutionContext

trait DatabaseSuite { self: GuiceOneAppPerSuite =>
  val db = app.injector.instanceOf(classOf[DatabaseConfigProvider]).get.db

  implicit val ctx: ExecutionContext =
    app.injector.instanceOf(classOf[ExecutionContext])

  import slick.jdbc.PostgresProfile.api._

  def timestamp = new Timestamp(System.currentTimeMillis())

  def uuid = UUID.randomUUID

  val faculties = TableQuery[FacultyTable]

  val teachingUnits = TableQuery[TeachingUnitTable]

  val graduations = TableQuery[GraduationTable]

  val studyPrograms = TableQuery[StudyProgramTable]

  def withSetup(
      f: DBIOAction[_, NoStream, Effect.All]*
  ) =
    db.run(
      DBIO.seq(
        sqlu"drop schema public cascade",
        sqlu"create schema public",
        faculties.schema.create,
        teachingUnits.schema.create,
        graduations.schema.create,
        studyPrograms.schema.create,
        DBIO.seq(f: _*)
      )
    )
}
