package database.repos

import database.tables.{CampusDbEntry, CampusTable}
import models.Campus
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class CampusRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Repository[Campus, CampusDbEntry, CampusTable] {

  import profile.api._

  protected val tableQuery = TableQuery[CampusTable]

  override protected def makeFilter = {
    case ("label", vs)        => t => t.hasLabel(vs.head)
    case ("abbreviation", vs) => t => t.hasAbbreviation(vs.head)
  }

  override protected def retrieveAtom(
      query: Query[CampusTable, CampusDbEntry, Seq]
  ) =
    retrieveDefault(query)

  override protected def toUniqueEntity(e: CampusDbEntry) = Campus(e)
}
