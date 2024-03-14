package models

import play.api.libs.json.{Json, Writes}

import java.util.UUID

case class Course(
    id: UUID,
    semester: UUID,
    module: UUID,
    part: ModulePart
) extends UniqueEntity[UUID]

object Course {
  implicit def writes: Writes[Course] = Json.writes
}
