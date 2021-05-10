/*
package database.repos

import database.tables.SemesterTable
import models.Semester
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class SemesterRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with Repository[Semester, SemesterTable] {

  import profile.api._

  protected val tableQuery = TableQuery[SemesterTable]

  override protected def makeFilter = { case ("label", vs) =>
    t => t.hasLabel(vs.head)
  }
}
*/
