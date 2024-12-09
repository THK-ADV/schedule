package database.repos

import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext

import database.repos.abstracts.Create
import database.repos.abstracts.Get
import database.tables.CampusTable
import models.Campus
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

@Singleton
final class CampusRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Get[UUID, Campus, CampusTable]
    with Create[UUID, Campus, CampusTable] {

  import profile.api._

  protected val tableQuery = TableQuery[CampusTable]

  protected override def uniqueCols(elem: Campus) =
    List(_.label === elem.label, _.abbrev === elem.abbrev)
}
