package models

import org.joda.time.{LocalDate, LocalTime}

import java.util.UUID

case class ScheduleJson(
    course: UUID,
    room: UUID,
    moduleExaminationRegulation: UUID,
    date: LocalDate,
    start: LocalTime,
    end: LocalTime,
    status: ScheduleEntryStatus
)
