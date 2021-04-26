package models

import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class TeachingUnitAssociationJson(faculty: UUID, teachingUnit: UUID)

object TeachingUnitAssociationJson {
  implicit val format: OFormat[TeachingUnitAssociationJson] =
    Json.format[TeachingUnitAssociationJson]
}
