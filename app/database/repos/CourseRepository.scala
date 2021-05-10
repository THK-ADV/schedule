/*
package database.repos

import database.tables.CourseTable
import models.Course
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class CourseRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Repository[Course, CourseTable] {

  import profile.api._

  protected val tableQuery = TableQuery[CourseTable]

  override protected def makeFilter = {
    case ("lecturer", vs)  => t => parseUUID(vs, t.hasUser)
    case ("semester", vs)  => t => parseUUID(vs, t.hasSemester)
    case ("subModule", vs) => t => parseUUID(vs, t.hasSubModule)
  }
}
*/
