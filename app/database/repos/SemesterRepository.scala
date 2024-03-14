package database.repos

import database.repos.abstracts.{Create, Get}
import database.tables.SemesterTable
import models.Semester
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class SemesterRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Get[UUID, Semester, SemesterTable]
    with Create[UUID, Semester, SemesterTable] {

  import profile.api._

  protected val tableQuery = TableQuery[SemesterTable]

  override protected def makeFilter = { case ("abbrev", vs) =>
    _.abbrev === vs.head
  }

  override protected def uniqueCols(elem: Semester) = {
    import database.tables.localDateColumnType
    List(
      _.label === elem.label,
      _.abbrev === elem.abbrev,
      _.start === elem.start,
      _.end === elem.end
    )
  }
}
