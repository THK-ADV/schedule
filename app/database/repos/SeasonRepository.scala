package database.repos

import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext

import database.repos.abstracts.Create
import database.repos.abstracts.Get
import database.tables.SeasonTable
import models.Season
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

@Singleton
final class SeasonRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Get[String, Season, SeasonTable]
    with Create[String, Season, SeasonTable] {

  import profile.api._

  protected val tableQuery = TableQuery[SeasonTable]

  protected override def makeFilter = {
    case ("deLabel", vs) =>
      _.deLabel === vs.head
  }
}
