package models

import play.api.libs.json.{Json, Writes}

import java.util.UUID

case class Course(
    id: UUID,
    semester: UUID,
    module: UUID,
    courseId: CourseId
) extends UniqueEntity[UUID]

object Course {
  implicit def writes: Writes[Course] = Json.writes
}
