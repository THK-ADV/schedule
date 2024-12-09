package database.repos

import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext

import database.repos.abstracts.Create
import database.repos.abstracts.Get
import database.tables.RoomTable
import models.Room
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

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
