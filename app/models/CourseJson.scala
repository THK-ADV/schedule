package models

import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class CourseJson(
    lecturer: UUID,
    semester: UUID,
    subModule: UUID,
    interval: CourseInterval,
    courseType: CourseType
)

object CourseJson {
  implicit val format: OFormat[CourseJson] = Json.format[CourseJson]
}
