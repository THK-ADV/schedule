package models

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

object Semester extends LocalDateFormat {
  implicit val format: OFormat[Semester] = Json.format[Semester]
}
