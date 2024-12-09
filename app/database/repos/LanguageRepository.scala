package database.repos

import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext

import database.repos.abstracts.Create
import database.repos.abstracts.Get
import database.tables.LanguageTable
import models.Language
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

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
