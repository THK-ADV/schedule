package models

import play.api.libs.json.{Json, Writes}

import java.util.UUID

case class ModuleInStudyProgram(
    id: UUID,
    module: UUID,
    studyProgram: UUID,
    mandatory: Boolean,
    focus: Boolean,
    recommendedSemester: List[Int],
    active: Boolean
) extends UniqueEntity[UUID]

object ModuleInStudyProgram {
  implicit def writes: Writes[ModuleInStudyProgram] = Json.writes
}
