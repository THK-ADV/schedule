package database.repos

import database.tables.{FacultyDbEntry, FacultyTable}
import models.Faculty
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class FacultyRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Repository[Faculty, FacultyDbEntry, FacultyTable] {

  import profile.api._

  protected val tableQuery = TableQuery[FacultyTable]

  override protected def makeFilter = {
    case ("label", vs)        => t => t.hasLabel(vs.head)
    case ("abbreviation", vs) => t => t.hasAbbreviation(vs.head)
  }

  override protected def retrieveAtom(
      query: Query[FacultyTable, FacultyDbEntry, Seq]
  ) =
    retrieveDefault(query)

  override protected def toUniqueEntity(e: FacultyDbEntry) =
    Faculty(e.label, e.abbreviation, e.id)
}
