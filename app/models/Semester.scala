package models

import org.joda.time.LocalDate

import java.util.UUID

case class Semester(
    id: UUID,
    label: String,
    abbrev: String,
    start: LocalDate,
    end: LocalDate,
    lectureStart: LocalDate,
    lectureEnd: LocalDate
) extends UniqueEntity[UUID]
