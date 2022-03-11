package models

import controllers.json.LocalDateFormat
import org.joda.time.LocalDate
import play.api.libs.json.{Json, OFormat}

case class SemesterJson(
    label: String,
    abbreviation: String,
    start: LocalDate,
    end: LocalDate,
    lectureStart: LocalDate,
    lectureEnd: LocalDate
)

object SemesterJson extends LocalDateFormat {
  implicit val format: OFormat[SemesterJson] = Json.format[SemesterJson]
}
