package models

import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class SubModule(
    module: UUID,
    label: String,
    abbreviation: String,
    mandatory: Boolean,
    recommendedSemester: Int,
    credits: Double,
    descriptionUrl: String,
    id: UUID
) extends UniqueEntity

object SubModule {
  implicit val format: OFormat[SubModule] = Json.format[SubModule]
}
