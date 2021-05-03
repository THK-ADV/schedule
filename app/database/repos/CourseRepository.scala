package database.repos

import database.tables.CourseTable
import models.Course
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import java.util.UUID
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
    case ("lecturer", vs) =>
      t => Option(UUID.fromString(vs.head)).map(t.hasUser) getOrElse false
    case ("semester", vs) =>
      t => Option(UUID.fromString(vs.head)).map(t.hasSemester) getOrElse false
    case ("subModule", vs) =>
      t => Option(UUID.fromString(vs.head)).map(t.hasSubModule) getOrElse false
  }
}
