package json

import models.Course
import play.api.libs.json.{Json, Writes}

trait CourseWrites extends ModulePartWrites {
  implicit val writes: Writes[Course] = Json.writes
}
