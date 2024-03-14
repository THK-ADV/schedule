package database.repos

import database.repos.abstracts.{Create, Get}
import database.tables.IdentityTable
import models.Identity
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class IdentityRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Get[String, Identity, IdentityTable]
    with Create[String, Identity, IdentityTable] {

  import profile.api._

  protected val tableQuery = TableQuery[IdentityTable]
}
