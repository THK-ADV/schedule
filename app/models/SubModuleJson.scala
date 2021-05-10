package models

import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class SubModuleJson(
    module: UUID,
    label: String,
    abbreviation: String,
    mandatory: Boolean,
    recommendedSemester: Int,
    credits: Double,
    descriptionUrl: String
)

object SubModuleJson {
  implicit val format: OFormat[SubModuleJson] = Json.format[SubModuleJson]
}
