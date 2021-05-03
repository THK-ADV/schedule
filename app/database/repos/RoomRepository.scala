package database.repos

import database.tables.RoomTable
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
    with Repository[Room, RoomTable] {

  import profile.api._

  protected val tableQuery = TableQuery[RoomTable]

  override protected def makeFilter = {
    case ("label", vs)  => t => t.hasLabel(vs.head)
    case ("number", vs) => t => t.hasNumber(vs.head)
  }
}
