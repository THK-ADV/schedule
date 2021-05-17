package models

import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class TeachingUnitJson(
    faculty: UUID,
    label: String,
    abbreviation: String,
    number: Int
)

object TeachingUnitJson {
  implicit val format: OFormat[TeachingUnitJson] = Json.format[TeachingUnitJson]
}
