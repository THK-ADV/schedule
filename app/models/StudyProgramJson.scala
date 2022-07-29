package models

import java.util.UUID

case class StudyProgramJson(
    teachingUnit: UUID,
    graduation: UUID,
    abbreviation: String,
    label: String,
    parent: Option[UUID]
)
