package database

import models.Semester
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._
import java.sql.Date
import java.util.UUID

class SemesterTable(tag: Tag)
    extends Table[Semester](tag, "semester")
    with SQLDateConverter
    with StartEndTable {

  def id = column[UUID]("id", O.PrimaryKey)
  def label = column[String]("label")
  def abbreviation = column[String]("abbreviation")

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
