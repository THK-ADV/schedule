package database.repos

import database.SQLDateConverter
import database.repos.filter.{
  CourseFilter,
  DateStartEndFilter,
  ModuleExaminationRegulationFilter,
  RoomFilter
}
import database.tables.{ScheduleDbEntry, ScheduleTable}
import models.Schedule.ScheduleAtom
import models._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class ScheduleRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Repository[Schedule, ScheduleDbEntry, ScheduleTable]
    with SQLDateConverter
    with RoomFilter[ScheduleTable]
    with DateStartEndFilter[ScheduleTable]
    with CourseFilter[ScheduleTable]
    with ModuleExaminationRegulationFilter[ScheduleTable] {

  import profile.api._

  protected val tableQuery = TableQuery[ScheduleTable]

  val scheduleFilter: PartialFunction[
    (String, Seq[String]),
    ScheduleTable => Rep[Boolean]
  ] = {
    case ("id", vs) =>
      _.id.inSet(vs.map(UUID.fromString))
    case ("courses", vs) =>
      _.courseFk.filter(_.id.inSet(vs.map(UUID.fromString))).exists
    case ("status", vs) =>
      _.hasStatus(ScheduleEntryStatus(vs.head))
  }

  val filter =
    List(
      allModuleExaminationRegulations,
      allCourse,
      dateStartEnd,
      allRooms,
      scheduleFilter
    )

  override protected val makeFilter =
    if (filter.isEmpty) PartialFunction.empty else filter.reduce(_ orElse _)

  override protected def retrieveAtom(
      query: Query[ScheduleTable, ScheduleDbEntry, Seq]
  ) = {
    val result = for {
      q <- query
      c <- q.courseFk
      cu <- c.userFk
      cse <- c.semesterFk
      csm <- c.subModuleFk
      r <- q.roomFk
      mer <- q.moduleExaminationRegulationFk
      merm <- mer.moduleFk
      merex <- mer.examinationRegulationFk
      mersp <- merex.studyProgramFk
      mertu <- mersp.teachingUnitFk
      merg <- mersp.graduationFk
    } yield (q, (c, cu, cse, csm), r, (mer, merm, merex, mersp, mertu, merg))

    val action = result.result.map(_.map {
      case (s, (c, cu, cse, csm), r, (mer, merm, merex, mersp, mertu, merg)) =>
        ScheduleAtom( // TODO adjust atomicness to actual needs
          Course(c, cu, cse, csm),
          Room(r),
          ModuleExaminationRegulation(mer, merm, merex, mersp, mertu, merg),
          s.date,
          s.start,
          s.end,
          s.status,
          s.id
        )
    })

    db.run(action)
  }

  override protected def toUniqueEntity(e: ScheduleDbEntry) = Schedule(e)
}
