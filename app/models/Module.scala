package models

import java.util.UUID

import play.api.libs.json.Json
import play.api.libs.json.Writes

case class Module(
    id: UUID,
    label: String,
    abbrev: String,
    language: String,
    season: String,
    parts: List[CourseId]
) extends UniqueEntity[UUID]

object Module {
  implicit def writes: Writes[Module] = Json.writes[Module]
}
