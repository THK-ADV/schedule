package database.repos

import database.tables.{LegalHoliday, LegalHolidayTable}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class LegalHolidayRepository @Inject() (
    val dbConfigProvider: DatabaseConfigProvider,
    implicit val ctx: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  protected val tableQuery = TableQuery[LegalHolidayTable]

  def createMany(xs: Seq[LegalHoliday]) =
    db.run(tableQuery ++= xs).map(_ => ())

  def delete(year: Int) =
    db.run(tableQuery.filter(_.year === year).delete)
}
