package models

import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class Module(
    examinationRegulation: UUID,
    label: String,
    abbreviation: String,
    credits: Double,
    descriptionUrl: String,
    id: UUID
) extends UniqueEntity

object Module {
  implicit val format: OFormat[Module] = Json.format[Module]
}
