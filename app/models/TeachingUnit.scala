package models

import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class TeachingUnit(
    label: String,
    abbreviation: String,
    number: Int,
    id: UUID
) extends UniqueEntity

object TeachingUnit {
  implicit val format: OFormat[TeachingUnit] = Json.format[TeachingUnit]
}
