package database.repos

import database.repos.abstracts.{Create, Get}
import database.tables.SeasonTable
import models.Season
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class SeasonRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Get[String, Season, SeasonTable]
    with Create[String, Season, SeasonTable] {

  import profile.api._

  protected val tableQuery = TableQuery[SeasonTable]

  override protected def makeFilter = { case ("deLabel", vs) =>
    _.deLabel === vs.head
  }
}
