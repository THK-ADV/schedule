package models

import play.api.libs.json.{Json, OFormat}

case class RoomJson(
    label: String,
    number: String,
    seats: Int,
    roomType: String
)

object RoomJson {
  implicit val format: OFormat[RoomJson] = Json.format[RoomJson]
}
