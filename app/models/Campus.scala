package models

import java.util.UUID

import play.api.libs.json.Json
import play.api.libs.json.Writes

case class Campus(id: UUID, label: String, abbrev: String) extends UniqueEntity[UUID]

object Campus {
  implicit def writes: Writes[Campus] = Json.writes
}
