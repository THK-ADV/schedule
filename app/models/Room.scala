package models

import play.api.libs.json.{Json, Writes}

import java.util.UUID

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
