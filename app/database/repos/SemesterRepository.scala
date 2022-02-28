package database.repos

import database.repos.filter.BooleanParser
import database.tables.{ScheduleTable, SemesterDbEntry, SemesterTable}
import models.Semester
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class SemesterRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Repository[Semester, SemesterDbEntry, SemesterTable]
    with BooleanParser {

  import profile.api._

  protected val tableQuery = TableQuery[SemesterTable]

  override protected def makeFilter = {
    case ("label", vs) =>
      _.hasLabel(vs.head)
    case ("select", vs) =>
      vs.head match {
        case "current" =>
          _.currentFilter()
        case "draft" =>
          semester =>
            TableQuery[ScheduleTable]
              .filter(schedule =>
                schedule.isDraft &&
                  schedule.courseFk
                    .filter(_.semester === semester.id)
                    .exists
              )
              .exists
        case _ =>
          _ => false
      }
  }

  override protected def retrieveAtom(
      query: Query[SemesterTable, SemesterDbEntry, Seq]
  ) =
    retrieveDefault(query)

  override protected def toUniqueEntity(e: SemesterDbEntry) = Semester(e)
}
