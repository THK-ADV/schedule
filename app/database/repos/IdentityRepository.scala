package database.repos

import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext

import database.repos.abstracts.Create
import database.repos.abstracts.Get
import database.tables.IdentityTable
import models.Identity
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

@Singleton
final class IdentityRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Get[String, Identity, IdentityTable]
    with Create[String, Identity, IdentityTable] {

  import profile.api._

  protected val tableQuery = TableQuery[IdentityTable]

  protected override def makeFilter = {
    case ("kind", xs) if xs.nonEmpty => _.kind === xs.head
  }
}
