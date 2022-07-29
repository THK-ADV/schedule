package models

import database.SQLDateConverter
import database.tables.SemesterDbEntry
import org.joda.time.LocalDate

import java.util.UUID

case class Semester(
    label: String,
    abbreviation: String,
    start: LocalDate,
    end: LocalDate,
    lectureStart: LocalDate,
    lectureEnd: LocalDate,
    id: UUID
) extends UniqueEntity

object Semester extends SQLDateConverter {
  def apply(db: SemesterDbEntry): Semester =
    Semester(
      db.label,
      db.abbreviation,
      db.start,
      db.end,
      db.lectureStart,
      db.lectureEnd,
      db.id
    )
}
