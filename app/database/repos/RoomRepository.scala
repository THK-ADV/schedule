package database.repos

import database.tables.{RoomDbEntry, RoomTable}
import models.Room
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class RoomRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Repository[Room, RoomDbEntry, RoomTable] {

  import profile.api._

  protected val tableQuery = TableQuery[RoomTable]

  override protected def makeFilter = {
    case ("label", vs)        => t => t.hasLabel(vs.head)
    case ("abbreviation", vs) => t => t.hasAbbreviation(vs.head)
  }

  override protected def retrieveAtom(
      query: Query[RoomTable, RoomDbEntry, Seq]
  ) =
    retrieveDefault(query)

  override protected def toUniqueEntity(e: RoomDbEntry) = Room(e)
}
