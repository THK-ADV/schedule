package models

import java.util.UUID

case class Course(
    id: UUID,
    semester: UUID,
    module: UUID,
    studyProgram: String,
    part: ModulePart
) extends UniqueEntity[UUID]
