package database.repos

import database.SQLDateConverter
import database.tables.{ScheduleDbEntry, ScheduleTable}
import models.Schedule.ScheduleAtom
import models.{Course, Room, Schedule}
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
    with SQLDateConverter {

  import profile.api._

  protected val tableQuery = TableQuery[ScheduleTable]

  override protected def makeFilter = {
    case ("course", vs) => t => parseUUID(vs, t.hasCourse)
    case ("room", vs)   => t => parseUUID(vs, t.hasRoom)
  }

  override protected def retrieveAtom(
      query: Query[ScheduleTable, ScheduleDbEntry, Seq]
  ) = {
    val result = for {
      q <- query
      c <- q.courseFk
      r <- q.roomFk
    } yield (q, c, r)

    val action = result.result.map(_.map { case (s, c, r) =>
      ScheduleAtom(Course(c), Room(r), s.date, s.start, s.end, s.id)
    })

    db.run(action)
  }

  override protected def toUniqueEntity(e: ScheduleDbEntry) = Schedule(e)
}
