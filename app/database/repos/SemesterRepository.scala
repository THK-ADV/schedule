package database.repos

import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext

import database.repos.abstracts.Create
import database.repos.abstracts.Get
import database.tables.SemesterTable
import models.Semester
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

@Singleton
final class SemesterRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Get[UUID, Semester, SemesterTable]
    with Create[UUID, Semester, SemesterTable] {

  import profile.api._

  protected val tableQuery = TableQuery[SemesterTable]

  protected override def makeFilter = {
    case ("abbrev", vs) =>
      _.abbrev === vs.head
  }

  protected override def uniqueCols(elem: Semester) = {
    import database.tables.localDateColumnType
    List(
      _.deLabel === elem.deLabel,
      _.enLabel === elem.enLabel,
      _.abbrev === elem.abbrev,
      _.start === elem.start,
      _.end === elem.end
    )
  }
}
