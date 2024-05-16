package database.tables
import org.joda.time.LocalDate
import slick.jdbc.PostgresProfile.api._

case class LegalHoliday(
    label: String,
    date: LocalDate,
    year: Int
)

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
