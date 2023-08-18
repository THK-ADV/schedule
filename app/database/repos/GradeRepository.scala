package database.repos

import database.repos.abstracts.{Create, Get}
import database.tables.GradeTable
import models.Grade
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class GradeRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Get[String, Grade, Grade, GradeTable]
    with Create[String, Grade, GradeTable] {

  import profile.api._

  protected val tableQuery = TableQuery[GradeTable]

  override protected def retrieveAtom(
      query: Query[GradeTable, Grade, Seq]
  ) =
    retrieveDefault(query)

  override protected def toUniqueEntity(e: Grade) = e
}
