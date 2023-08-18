package models

import localization.LocalizedLabel
import org.joda.time.LocalDate

import java.util.UUID

case class StudyProgram(
    id: String,
    teachingUnit: UUID,
    grade: String,
    deLabel: String,
    enLabel: String,
    abbrev: String,
    poNumber: Int,
    start: LocalDate,
    end: Option[LocalDate]
) extends UniqueEntity[String]
    with LocalizedLabel
