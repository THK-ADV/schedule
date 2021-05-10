package models

import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class StudyProgramJson(
    teachingUnit: UUID,
    graduation: UUID,
    abbreviation: String,
    label: String
)

object StudyProgramJson {
  implicit val format: OFormat[StudyProgramJson] = Json.format[StudyProgramJson]
}
