package database.repos

import database.repos.abstracts.{Create, Get}
import database.tables.LanguageTable
import models.Language
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class LanguageRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Get[String, Language, LanguageTable]
    with Create[String, Language, LanguageTable] {

  import profile.api._

  protected val tableQuery = TableQuery[LanguageTable]
}
