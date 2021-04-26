package models

import play.api.libs.json.{Json, OFormat}

case class StudyProgramJson(
    label: String,
    abbreviation: String,
    graduation: String
)

object StudyProgramJson {
  implicit val format: OFormat[StudyProgramJson] = Json.format[StudyProgramJson]
}
