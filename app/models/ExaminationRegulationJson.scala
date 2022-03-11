package models

import controllers.json.{JsonNullWritable, LocalDateFormat}
import org.joda.time.LocalDate
import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class ExaminationRegulationJson(
    studyProgram: UUID,
    number: Int,
    start: LocalDate,
    end: Option[LocalDate]
)

object ExaminationRegulationJson extends LocalDateFormat with JsonNullWritable {
  implicit val format: OFormat[ExaminationRegulationJson] =
    Json.format[ExaminationRegulationJson]
}
