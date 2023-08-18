package json

import models.Semester
import play.api.libs.json.{Json, Writes}

trait SemesterWrites extends LocalDateFormat {
  implicit val writes: Writes[Semester] = Json.writes
}
