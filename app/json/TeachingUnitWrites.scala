package json

import models.TeachingUnit
import play.api.libs.json.{Json, Writes}

trait TeachingUnitWrites {
  implicit val writes: Writes[TeachingUnit] =
    Json.writes[TeachingUnit]
}
