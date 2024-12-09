package database.tables

import java.time.LocalDate

import database.tables.localDateColumnType
import database.UUIDUniqueColumn
import models.Semester
import slick.jdbc.PostgresProfile.api._

final class SemesterTable(tag: Tag) extends Table[Semester](tag, "semester") with UUIDUniqueColumn {

  def deLabel = column[String]("de_label")

  def enLabel = column[String]("en_label")

  def abbrev = column[String]("abbrev")

  def start = column[LocalDate]("start")

  def end = column[LocalDate]("end")

  def lectureStart = column[LocalDate]("lecture_start")

  def lectureEnd = column[LocalDate]("lecture_end")

  def * = (
    id,
    deLabel,
    enLabel,
    abbrev,
    start,
    end,
    lectureStart,
    lectureEnd
  ) <> (Semester.apply, Semester.unapply)
}
