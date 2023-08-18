package models

import java.util.UUID

case class ModuleInStudyProgram(
    id: UUID,
    module: UUID,
    studyProgram: String,
    mandatory: Boolean,
    recommendedSemester: List[Int]
) extends UniqueEntity[UUID]
