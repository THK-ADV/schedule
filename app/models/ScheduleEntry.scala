package models

import org.joda.time.LocalDateTime

import java.util.UUID

case class ScheduleEntry(
    id: UUID,
    course: UUID,
    start: LocalDateTime,
    end: LocalDateTime
) extends UniqueEntity[UUID]
