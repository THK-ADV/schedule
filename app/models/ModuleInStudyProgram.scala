package models

import java.util.UUID

import play.api.libs.json.Json
import play.api.libs.json.Writes

case class ModuleInStudyProgram(
    id: UUID,
    module: UUID,
    studyProgram: UUID,
    mandatory: Boolean,
    focus: Boolean,
    recommendedSemester: List[Int]
) extends UniqueEntity[UUID]

object ModuleInStudyProgram {
  implicit def writes: Writes[ModuleInStudyProgram] = Json.writes
}
