package models

import date.LocalDateFormat
import org.joda.time.LocalDate
import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class ExaminationRegulation(
    studyProgram: UUID,
    label: String,
    abbreviation: String,
    start: LocalDate,
    end: LocalDate,
    id: UUID
) extends UniqueEntity

object ExaminationRegulation extends LocalDateFormat {
  implicit val format: OFormat[ExaminationRegulation] =
    Json.format[ExaminationRegulation]
}
