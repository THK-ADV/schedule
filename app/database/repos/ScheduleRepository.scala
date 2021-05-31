package database.repos

import database.SQLDateConverter
import database.repos.filter.{DateStartEndFilter, RoomFilter}
import database.tables.{ScheduleDbEntry, ScheduleTable}
import models.Schedule.ScheduleAtom
import models.{Course, ModuleExaminationRegulation, Room, Schedule}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class ScheduleRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Repository[Schedule, ScheduleDbEntry, ScheduleTable]
    with SQLDateConverter
    with FilterValueParser
    with RoomFilter[ScheduleTable]
    with DateStartEndFilter[ScheduleTable] {

  import profile.api._

  protected val tableQuery = TableQuery[ScheduleTable]

  val filter = List(room, dateStartEnd)

  override protected val makeFilter =
    if (filter.isEmpty) PartialFunction.empty else filter.reduce(_ orElse _)

  override protected def retrieveAtom(
      query: Query[ScheduleTable, ScheduleDbEntry, Seq]
  ) = {
    val result = for {
      q <- query
      c <- q.courseFk
      r <- q.roomFk
      mer <- q.moduleExaminationRegulationFk
    } yield (q, c, r, mer)

    val action = result.result.map(_.map { case (s, c, r, mer) =>
      ScheduleAtom(
        Course(c),
        Room(r),
        ModuleExaminationRegulation(mer),
        s.date,
        s.start,
        s.end,
        s.id
      )
    })

    db.run(action)
  }

  override protected def toUniqueEntity(e: ScheduleDbEntry) = Schedule(e)
}
