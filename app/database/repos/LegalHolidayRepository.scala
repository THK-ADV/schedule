package database.repos

import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext

import database.tables.LegalHolidayTable
import models.LegalHoliday
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

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

  def all(from: LocalDate, to: LocalDate) =
    db.run(tableQuery.filter(a => a.date >= from && a.date <= to).result)
}
