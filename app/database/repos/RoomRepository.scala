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
    with Get[UUID, Room, RoomTable]
    with Create[UUID, Room, RoomTable] {

  import profile.api._

  protected val tableQuery = TableQuery[RoomTable]
}
