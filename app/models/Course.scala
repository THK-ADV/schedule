package models

import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class Course(
    lecturer: UUID,
    semester: UUID,
    subModule: UUID,
    interval: String,
    courseType: String,
    id: UUID
) extends UniqueEntity

object Course {
  implicit val format: OFormat[Course] = Json.format[Course]
}
