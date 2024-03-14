package models

import play.api.libs.json.{Json, Writes}

import java.util.UUID

case class Module(
    id: UUID,
    label: String,
    abbrev: String,
    language: String,
    season: String,
    parts: List[ModulePart]
) extends UniqueEntity[UUID]

object Module {
  implicit def writes: Writes[Module] = Json.writes[Module]
}
