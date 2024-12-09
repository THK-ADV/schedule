package models

import java.util.UUID

import play.api.libs.json.Json
import play.api.libs.json.Writes

case class Room(
    id: UUID,
    campus: UUID,
    label: String,
    identifier: String,
    roomType: String,
    capacity: Int
) extends UniqueEntity[UUID]

object Room {
  implicit def writes: Writes[Room] = Json.writes
}
