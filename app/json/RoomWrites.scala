package json

import models.Room
import play.api.libs.json.{Json, Writes}

trait RoomWrites {
  implicit val writes: Writes[Room] = Json.writes
}
