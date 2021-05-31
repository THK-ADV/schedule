package models

import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class SubModuleJson(
    module: UUID,
    label: String,
    abbreviation: String,
    recommendedSemester: Int,
    credits: Double,
    descriptionUrl: String,
    language: Language,
    season: Season
)

object SubModuleJson {
  implicit val format: OFormat[SubModuleJson] = Json.format[SubModuleJson]
}
