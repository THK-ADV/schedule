package database.repos

import database.repos.abstracts.{Create, Get}
import database.tables.DegreeTable
import models.Degree
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class DegreeRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Get[String, Degree, DegreeTable]
    with Create[String, Degree, DegreeTable] {

  import profile.api._

  protected val tableQuery = TableQuery[DegreeTable]
}
