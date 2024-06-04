package database.tables
import models.LegalHoliday
import org.joda.time.LocalDate
import slick.jdbc.PostgresProfile.api._

final class LegalHolidayTable(tag: Tag)
    extends Table[LegalHoliday](tag, "legal_holiday") {

  import database.tables.localDateColumnType

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
