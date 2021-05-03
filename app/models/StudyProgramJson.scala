package models

import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class StudyProgramJson(
    teachingUnit: UUID,
    label: String,
    abbreviation: String,
    graduation: String
)

object StudyProgramJson {
  implicit val format: OFormat[StudyProgramJson] = Json.format[StudyProgramJson]
}
