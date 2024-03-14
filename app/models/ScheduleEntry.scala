package models

import org.joda.time.{LocalDate, LocalTime}

import java.util.UUID

case class ScheduleEntry(
    id: UUID,
    course: UUID,
    room: UUID,
    date: LocalDate,
    start: LocalTime,
    end: LocalTime
) extends UniqueEntity[UUID]
