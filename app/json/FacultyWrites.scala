package json

import models.Faculty
import play.api.libs.json.{Json, Writes}

trait FacultyWrites {
  implicit val writes: Writes[Faculty] = Json.writes[Faculty]
}
