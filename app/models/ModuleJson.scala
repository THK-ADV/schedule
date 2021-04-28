package models

import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class ModuleJson(
    examinationRegulation: UUID,
    label: String,
    abbreviation: String,
    credits: Double,
    descriptionUrl: String
)

object ModuleJson {
  implicit val format: OFormat[ModuleJson] = Json.format[ModuleJson]
}
