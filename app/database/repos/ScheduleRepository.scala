package database.repos

import database.SQLDateConverter
import database.repos.filter.{
  CourseFilter,
  DateStartEndFilter,
  ModuleExaminationRegulationFilter,
  RoomFilter
}
import database.tables._
import models.Schedule.ScheduleAtom
import models.StudyProgram.StudyProgramAtom
import models._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class ScheduleRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    val spRepo: StudyProgramRepository,
    val cRepo: CourseRepository,
    val merRepo: ModuleExaminationRegulationRepository,
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

  private val scheduleFilter: PartialFunction[
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

  private val filter =
    List(
      allModuleExaminationRegulations,
      allCourse,
      dateStartEnd,
      allRooms,
      scheduleFilter
    )

  override protected val makeFilter =
    if (filter.isEmpty) PartialFunction.empty else filter.reduce(_ orElse _)

  def collectDependencies(s: ScheduleTable) =
    for {
      q <- tableQuery.filter(_.id === s.id)
      r <- q.roomFk
      c <- q.courseFk
      u <- c.userFk
      s <- c.semesterFk
      sm <- c.subModuleFk
      mer <- q.moduleExaminationRegulationFk.flatMap(
        merRepo.collectDependencies
      )
    } yield (q, (c, u, s, sm), r, mer)

  override protected def retrieveAtom(
      query: Query[ScheduleTable, ScheduleDbEntry, Seq]
  ) =
    db.run {
      query
        .flatMap(collectDependencies)
        .result
        .flatMap { elems =>
          DBIO.sequence(
            elems.map { e =>
              spRepo
                .getRecursive(Seq(e._4._3._2))
                .map(sp =>
                  makeAtom(
                    e._1,
                    e._2,
                    e._3,
                    (e._4._1, e._4._2, e._4._3._1, sp.head)
                  )
                )
            }
          )
        }
    }

  def makeAtom(
      s: ScheduleDbEntry,
      c: (CourseDbEntry, UserDbEntry, SemesterDbEntry, SubModuleDbEntry),
      r: RoomDbEntry,
      mer: (
          ModuleExaminationRegulationDbEntry,
          ModuleDbEntry,
          ExaminationRegulationDbEntry,
          StudyProgramAtom
      )
  ) = ScheduleAtom(
    cRepo.makeAtom(c),
    Room(r),
    merRepo.makeAtom(mer._1, mer._2, mer._3, mer._4),
    s.date,
    s.start,
    s.end,
    s.status,
    s.id
  )

  override protected def toUniqueEntity(e: ScheduleDbEntry) = Schedule(e)
}
