package models

import org.joda.time.LocalDate

case class SemesterJson(
    label: String,
    abbreviation: String,
    start: LocalDate,
    end: LocalDate,
    lectureStart: LocalDate,
    lectureEnd: LocalDate
)
