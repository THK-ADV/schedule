package database.repos

import database.tables.LegalHolidayTable
import models.LegalHoliday
import org.joda.time.LocalDate
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

  def all(from: LocalDate, to: LocalDate) = {
    import database.tables.localDateColumnType
    db.run(tableQuery.filter(a => a.date >= from && a.date <= to).result)
  }
}
