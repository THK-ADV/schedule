package json

import models.Grade
import play.api.libs.json.{Json, Writes}

trait GradeWrites {
  implicit val writes: Writes[Grade] = Json.writes[Grade]
}
