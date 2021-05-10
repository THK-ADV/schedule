package models

import play.api.libs.json.{Json, OFormat}

case class RoomJson(
    label: String,
    abbreviation: String
)

object RoomJson {
  implicit val format: OFormat[RoomJson] = Json.format[RoomJson]
}
