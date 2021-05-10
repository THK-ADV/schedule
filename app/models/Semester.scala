package models

import database.SQLDateConverter
import database.tables.SemesterDbEntry
import date.LocalDateFormat
import org.joda.time.LocalDate
import play.api.libs.json._

import java.util.UUID

case class Semester(
    label: String,
    abbreviation: String,
    start: LocalDate,
    end: LocalDate,
    lectureStart: LocalDate,
    lectureEnd: LocalDate,
    id: UUID
) extends UniqueEntity

object Semester extends LocalDateFormat with SQLDateConverter {
  implicit val format: OFormat[Semester] = Json.format[Semester]

  def apply(db: SemesterDbEntry): Semester =
    Semester(
      db.label,
      db.abbreviation,
      db.start,
      db.end,
      db.lectureStart,
      db.lectureEnd,
      db.id
    )
}
