package models

import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class RoomJson(
    campus: UUID,
    label: String,
    abbreviation: String
)

object RoomJson {
  implicit val format: OFormat[RoomJson] = Json.format[RoomJson]
}
