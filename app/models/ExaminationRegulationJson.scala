package models

import date.LocalDateFormat
import org.joda.time.LocalDate
import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class ExaminationRegulationJson(
    studyProgram: UUID,
    label: String,
    abbreviation: String,
    start: LocalDate,
    end: LocalDate
)

object ExaminationRegulationJson extends LocalDateFormat {
  implicit val format: OFormat[ExaminationRegulationJson] =
    Json.format[ExaminationRegulationJson]
}
