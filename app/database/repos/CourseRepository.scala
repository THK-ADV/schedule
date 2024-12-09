package database.repos

import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import database.repos.abstracts.Create
import database.repos.abstracts.Get
import database.tables.CourseTable
import models.Course
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

@Singleton
final class CourseRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Get[UUID, Course, CourseTable]
    with Create[UUID, Course, CourseTable] {

  import profile.api._

  protected val tableQuery = TableQuery[CourseTable]

  protected override def uniqueCols(elem: Course) = {
    import database.tables.modulePartColumnType
    List(
      _.semester === elem.semester,
      _.module === elem.module,
      _.courseId === elem.courseId
    )
  }

  def deleteAll(): Future[Int] =
    db.run(tableQuery.delete)
}
