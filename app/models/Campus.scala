package models

import play.api.libs.json.{Json, Writes}

import java.util.UUID

case class Campus(id: UUID, label: String, abbrev: String)
    extends UniqueEntity[UUID]

object Campus {
  implicit def writes: Writes[Campus] = Json.writes
}
