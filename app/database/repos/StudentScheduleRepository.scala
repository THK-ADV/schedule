/*
package database.repos

import database.tables.StudentScheduleTable
import models.StudentSchedule
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class StudentScheduleRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Repository[StudentSchedule, StudentScheduleTable] {

  import profile.api._

  protected val tableQuery = TableQuery[StudentScheduleTable]

  override protected def makeFilter = {
    case ("student", vs)  => t => parseUUID(vs, t.hasUser)
    case ("schedule", vs) => t => parseUUID(vs, t.hasSchedule)
  }
}
*/
