package models

import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class CourseJson(
    lecturer: UUID,
    semester: UUID,
    subModule: UUID,
    interval: String,
    courseType: String
)

object CourseJson {
  implicit val format: OFormat[CourseJson] = Json.format[CourseJson]
}
