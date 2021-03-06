package database.repos

import database.tables.{GraduationDbEntry, GraduationTable}
import models.Graduation
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class GraduationRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Repository[Graduation, GraduationDbEntry, GraduationTable] {

  import profile.api._

  protected val tableQuery = TableQuery[GraduationTable]

  override protected def makeFilter = {
    case ("label", vs)        => t => t.hasLabel(vs.head)
    case ("abbreviation", vs) => t => t.hasAbbreviation(vs.head)
  }

  override protected def retrieveAtom(
      query: Query[GraduationTable, GraduationDbEntry, Seq]
  ) =
    retrieveDefault(query)

  override protected def toUniqueEntity(e: GraduationDbEntry) =
    Graduation(e.label, e.abbreviation, e.id)
}
