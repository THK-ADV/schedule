package database.repos

import database.tables.{StudentScheduleDbEntry, StudentScheduleTable}
import models.StudentSchedule.StudentScheduleAtom
import models.{Schedule, StudentSchedule, User}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class StudentScheduleRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Repository[
      StudentSchedule,
      StudentScheduleDbEntry,
      StudentScheduleTable
    ] {

  import profile.api._

  protected val tableQuery = TableQuery[StudentScheduleTable]

  override protected def makeFilter = {
    case ("student", vs)  => t => parseUUID(vs, t.hasUser)
    case ("schedule", vs) => t => parseUUID(vs, t.hasSchedule)
  }

  override protected def retrieveAtom(
      query: Query[StudentScheduleTable, StudentScheduleDbEntry, Seq]
  ) = {
    val result = for {
      q <- query
      u <- q.userFk
      s <- q.scheduleFk
    } yield (q, u, s)

    val action = result.result.map(_.map { case (ss, u, s) =>
      StudentScheduleAtom(User(u), Schedule(s), ss.id)
    })

    db.run(action)
  }

  override protected def toUniqueEntity(e: StudentScheduleDbEntry) =
    StudentSchedule(e)
}
