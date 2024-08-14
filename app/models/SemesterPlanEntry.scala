package models

import java.time.LocalDateTime
import java.util.UUID

case class SemesterPlanEntry(
    id: UUID,
    start: LocalDateTime,
    end: LocalDateTime,
    entryType: SemesterPlanEntryType,
    semester: UUID,
    teachingUnit: Option[UUID],
    semesterIndex: Option[String]
) extends UniqueEntity[UUID]
