package models

import database.tables.RoomDbEntry
import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class Room(
    label: String,
    abbreviation: String,
    id: UUID
) extends UniqueEntity

object Room {
  implicit val format: OFormat[Room] = Json.format[Room]

  def apply(db: RoomDbEntry): Room = Room(db.label, db.abbreviation, db.id)
}
