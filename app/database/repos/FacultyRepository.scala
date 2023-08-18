package database.repos

import database.repos.abstracts.{Create, Get}
import database.tables.FacultyTable
import models.Faculty
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class FacultyRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Get[String, Faculty, Faculty, FacultyTable]
    with Create[String, Faculty, FacultyTable] {

  import profile.api._

  protected val tableQuery = TableQuery[FacultyTable]

  override protected def retrieveAtom(
      query: Query[FacultyTable, Faculty, Seq]
  ) =
    retrieveDefault(query)

  override protected def toUniqueEntity(e: Faculty) = e
}
