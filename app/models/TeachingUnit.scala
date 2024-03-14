package models

import localization.LocalizedLabel
import play.api.libs.json.{Json, Writes}

import java.util.UUID

case class TeachingUnit(
    id: UUID,
    faculty: String,
    deLabel: String,
    enLabel: String
) extends UniqueEntity[UUID]
    with LocalizedLabel

object TeachingUnit {
  implicit def writes: Writes[TeachingUnit] = Json.writes[TeachingUnit]
}
