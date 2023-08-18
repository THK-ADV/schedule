package database.repos

import database.repos.abstracts.{Create, Get}
import database.tables.CourseTable
import models.Course
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class CourseRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Get[UUID, Course, Course, CourseTable]
    with Create[UUID, Course, CourseTable] {

  import profile.api._

  protected val tableQuery = TableQuery[CourseTable]

  override protected def retrieveAtom(
      query: Query[CourseTable, Course, Seq]
  ) =
    retrieveDefault(query)

  override protected def toUniqueEntity(e: Course) = e

  override protected def uniqueCols(elem: Course) = {
    import database.tables.modulePartColumnType
    List(
      _.semester === elem.semester,
      _.module === elem.module,
      _.studyProgram === elem.studyProgram,
      _.modulePart === elem.part
    )
  }
}
