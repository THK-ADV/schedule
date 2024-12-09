package models

import java.util.UUID

import play.api.libs.json.Json
import play.api.libs.json.Writes

case class Course(
    id: UUID,
    semester: UUID,
    module: UUID,
    courseId: CourseId
) extends UniqueEntity[UUID]

object Course {
  implicit def writes: Writes[Course] = Json.writes
}
