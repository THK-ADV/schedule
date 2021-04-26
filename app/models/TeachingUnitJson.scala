package models

import play.api.libs.json.{Json, OFormat}

case class TeachingUnitJson(
    label: String,
    abbreviation: String,
    number: Int
)

object TeachingUnitJson {
  implicit val format: OFormat[TeachingUnitJson] = Json.format[TeachingUnitJson]
}
