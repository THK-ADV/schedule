package database.repos

import database.repos.filter.{AbbreviationFilter, CampusFilter, LabelFilter}
import database.tables.{RoomDbEntry, RoomTable}
import models.Room.RoomAtom
import models.{Campus, Room}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class RoomRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Repository[Room, RoomDbEntry, RoomTable]
    with AbbreviationFilter[RoomTable]
    with LabelFilter[RoomTable]
    with CampusFilter[RoomTable] {

  import profile.api._

  protected val tableQuery = TableQuery[RoomTable]

  override protected def makeFilter =
    PartialFunction.fromFunction(abbrev orElse label orElse campus)

  override protected def retrieveAtom(
      query: Query[RoomTable, RoomDbEntry, Seq]
  ) = {
    val result = for {
      q <- query
      c <- q.campusFk
    } yield (q, c)

    val action = result.result.map(_.map { case (r, c) =>
      RoomAtom(
        Campus(c),
        r.label,
        r.abbreviation,
        r.id
      )
    })

    db.run(action)
  }

  override protected def toUniqueEntity(e: RoomDbEntry) = Room(e)
}
