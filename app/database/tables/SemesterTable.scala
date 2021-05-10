package database.tables

import database.SQLDateConverter
import database.cols.{
  AbbreviationColumn,
  UniqueEntityColumn,
  LabelColumn,
  StartEndColumn
}
import models.Semester
import slick.jdbc.PostgresProfile.api._

import java.sql.Date
import java.util.UUID

class SemesterTable(tag: Tag)
    extends Table[Semester](tag, "semester")
    with UniqueEntityColumn
    with SQLDateConverter
    with LabelColumn
    with AbbreviationColumn
    with StartEndColumn {

  def lectureStart = column[Date]("lecture_start")

  def lectureEnd = column[Date]("lecture_end")

  def * = (
    label,
    abbreviation,
    start,
    end,
    lectureStart,
    lectureEnd,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: ((String, String, Date, Date, Date, Date, UUID)) => Semester = {
    case (label, abbreviation, start, end, lectureStart, lectureEnd, id) =>
      Semester(label, abbreviation, start, end, lectureStart, lectureEnd, id)
  }

  def unmapRow
      : Semester => Option[(String, String, Date, Date, Date, Date, UUID)] = {
    s =>
      Option(
        (
          s.label,
          s.abbreviation,
          s.start,
          s.end,
          s.lectureStart,
          s.lectureEnd,
          s.id
        )
      )
  }
}
