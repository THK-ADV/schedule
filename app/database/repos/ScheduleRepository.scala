package database.repos

import database.tables.ScheduleTable
import models.Schedule
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class ScheduleRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Repository[Schedule, ScheduleTable] {

  import profile.api._

  protected val tableQuery = TableQuery[ScheduleTable]

  override protected def makeFilter = {
    case ("course", vs) => t => parseUUID(vs, t.hasCourse)
    case ("room", vs)   => t => parseUUID(vs, t.hasRoom)
  }
}
