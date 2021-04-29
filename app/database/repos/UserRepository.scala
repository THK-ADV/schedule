package database.repos

import database.tables.UserTable
import models.User
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class UserRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Repository[User, UserTable] {

  import profile.api._

  protected val tableQuery = TableQuery[UserTable]

  override protected def makeFilter = {
    case ("firstname", vs) =>
      t => t.firstname.toLowerCase === vs.head.toLowerCase()
    case ("lastname", vs) =>
      t => t.lastname.toLowerCase === vs.head.toLowerCase()
    case ("status", vs) => t => t.status.toLowerCase === vs.head.toLowerCase()
  }
}
