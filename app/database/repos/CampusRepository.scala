package database.repos

import database.repos.abstracts.{Create, Get}
import database.tables.CampusTable
import models.Campus
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class CampusRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Get[UUID, Campus, CampusTable]
    with Create[UUID, Campus, CampusTable] {

  import profile.api._

  protected val tableQuery = TableQuery[CampusTable]

  override protected def uniqueCols(elem: Campus) =
    List(_.label === elem.label, _.abbrev === elem.abbrev)
}
