package json

import models.Campus
import play.api.libs.json.{Json, Writes}

trait CampusWrites {
  implicit val writes: Writes[Campus] = Json.writes
}
