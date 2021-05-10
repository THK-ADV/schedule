package database.tables

import database.UniqueDbEntry
import database.cols.{
  AbbreviationColumn,
  LabelColumn,
  StartEndColumn,
  UniqueEntityColumn
}
import slick.jdbc.PostgresProfile.api._

import java.sql.{Date, Timestamp}
import java.util.UUID

case class SemesterDbEntry(
    label: String,
    abbreviation: String,
    start: Date,
    end: Date,
    lectureStart: Date,
    lectureEnd: Date,
    lastModified: Timestamp,
    id: UUID
) extends UniqueDbEntry

class SemesterTable(tag: Tag)
    extends Table[SemesterDbEntry](tag, "semester")
    with UniqueEntityColumn
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
    lastModified,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: (
      (String, String, Date, Date, Date, Date, Timestamp, UUID)
  ) => SemesterDbEntry = {
    case (
          label,
          abbreviation,
          start,
          end,
          lectureStart,
          lectureEnd,
          lastModified,
          id
        ) =>
      SemesterDbEntry(
        label,
        abbreviation,
        start,
        end,
        lectureStart,
        lectureEnd,
        lastModified,
        id
      )
  }

  def unmapRow: SemesterDbEntry => Option[
    (String, String, Date, Date, Date, Date, Timestamp, UUID)
  ] = { s =>
    Option(
      (
        s.label,
        s.abbreviation,
        s.start,
        s.end,
        s.lectureStart,
        s.lectureEnd,
        s.lastModified,
        s.id
      )
    )
  }
}
