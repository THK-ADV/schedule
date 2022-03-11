package models

import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class ModuleExaminationRegulationJson(
    module: UUID,
    examinationRegulation: UUID,
    mandatory: Boolean
)

object ModuleExaminationRegulationJson {
  implicit val format: OFormat[ModuleExaminationRegulationJson] =
    Json.format[ModuleExaminationRegulationJson]
}
