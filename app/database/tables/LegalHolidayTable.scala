package database.tables
import database.tables.localDateColumnType
import models.LegalHoliday
import slick.jdbc.PostgresProfile.api._

import java.time.LocalDate

final class LegalHolidayTable(tag: Tag)
    extends Table[LegalHoliday](tag, "legal_holiday") {

  def label = column[String]("label", O.PrimaryKey)

  def date = column[LocalDate]("date", O.PrimaryKey)(localDateColumnType)

  def year = column[Int]("year")

  def * =
    (
      label,
      date,
      year
    ) <> ((LegalHoliday.apply _).tupled, LegalHoliday.unapply)
}
