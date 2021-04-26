package models

import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class TeachingUnitAssociation(faculty: UUID, teachingUnit: UUID, id: UUID)
    extends UniqueEntity

object TeachingUnitAssociation {
  implicit val format: OFormat[TeachingUnitAssociation] =
    Json.format[TeachingUnitAssociation]
}
