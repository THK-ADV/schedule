package database.tables

import database.SQLDateConverter
import database.cols.{AbbreviationColumn, IDColumn, LabelColumn, StartEndColumn}
import models.Semester
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

import java.sql.Date
import java.util.UUID

class SemesterTable(tag: Tag)
    extends Table[Semester](tag, "semester")
    with IDColumn
    with SQLDateConverter
    with LabelColumn
    with AbbreviationColumn
    with StartEndColumn {

  def examStart = column[Date]("exam_start")

  def * = (
    label,
    abbreviation,
    start,
    end,
    examStart,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: ((String, String, Date, Date, Date, UUID)) => Semester = {
    case (label, abbreviation, start, end, examStart, id) =>
      Semester(label, abbreviation, start, end, examStart, id)
  }

  def unmapRow: Semester => Option[(String, String, Date, Date, Date, UUID)] = {
    s =>
      Option((s.label, s.abbreviation, s.start, s.end, s.examStart, s.id))
  }
}
