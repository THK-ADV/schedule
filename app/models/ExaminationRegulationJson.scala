package models

import org.joda.time.LocalDate

import java.util.UUID

case class ExaminationRegulationJson(
    studyProgram: UUID,
    number: Int,
    start: LocalDate,
    end: Option[LocalDate]
)
