package json

import models.Season
import play.api.libs.json.{Json, Writes}

trait SeasonWrites {
  implicit val writes: Writes[Season] =
    Json.writes[Season]
}
