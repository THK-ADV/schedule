package suite

import database.tables._
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

  val graduations = TableQuery[GradeTable]

  val studyPrograms = TableQuery[StudyProgramTable]

  val examinationRegulations = TableQuery[ExaminationRegulationTable]

  val users = TableQuery[UserTable]

  val modules = TableQuery[ModuleTable]

  val submodules = TableQuery[SubModuleTable]

  val moduleExaminationRegulations =
    TableQuery[ModuleExaminationRegulationTable]

  val semesters = TableQuery[SemesterTable]

  val courses = TableQuery[CourseTable]

  val schedules = TableQuery[ScheduleEntryTable]

  val rooms = TableQuery[RoomTable]

  val campus = TableQuery[CampusTable]

  def withFreshDb(
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
        examinationRegulations.schema.create,
        users.schema.create,
        modules.schema.create,
        moduleExaminationRegulations.schema.create,
        submodules.schema.create,
        semesters.schema.create,
        courses.schema.create,
        campus.schema.create,
        rooms.schema.create,
        schedules.schema.create,
        DBIO.seq(f: _*)
      )
    )
}
