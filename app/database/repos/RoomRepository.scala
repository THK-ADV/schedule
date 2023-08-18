package database.repos

import database.repos.abstracts.{Create, Get}
import database.tables.RoomTable
import models.Room
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class RoomRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Get[UUID, Room, Room, RoomTable]
    with Create[UUID, Room, RoomTable] {

  import profile.api._

  protected val tableQuery = TableQuery[RoomTable]

  override protected def retrieveAtom(
      query: Query[RoomTable, Room, Seq]
  ) =
    retrieveDefault(query)

  override protected def toUniqueEntity(e: Room) = e

  override protected def uniqueCols(elem: Room) =
    List(_.identifier === elem.identifier, _.campus === elem.campus)
}
