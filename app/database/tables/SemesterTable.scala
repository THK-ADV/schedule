package database.tables

import database.cols.UUIDUniqueColumn
import models.Semester
import org.joda.time.LocalDate
import slick.jdbc.PostgresProfile.api._

final class SemesterTable(tag: Tag)
    extends Table[Semester](tag, "semester")
    with UUIDUniqueColumn {

  import database.tables.localDateColumnType

  def label = column[String]("label")

  def abbrev = column[String]("abbrev")

  def start = column[LocalDate]("start")

  def end = column[LocalDate]("end")

  def lectureStart = column[LocalDate]("lecture_start")

  def lectureEnd = column[LocalDate]("lecture_end")

  def * = (
    id,
    label,
    abbrev,
    start,
    end,
    lectureStart,
    lectureEnd
  ) <> (Semester.tupled, Semester.unapply)
}
