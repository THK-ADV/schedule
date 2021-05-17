package database.tables

import database.UniqueDbEntry
import database.cols.{
  AbbreviationColumn,
  LabelColumn,
  NumberColumn,
  UniqueEntityColumn
}
import slick.jdbc.PostgresProfile.api._

import java.sql.Timestamp
import java.util.UUID

case class FacultyDbEntry(
    label: String,
    abbreviation: String,
    number: Int,
    lastModified: Timestamp,
    id: UUID
) extends UniqueDbEntry

class FacultyTable(tag: Tag)
    extends Table[FacultyDbEntry](tag, "faculty")
    with UniqueEntityColumn
    with AbbreviationColumn
    with LabelColumn
    with NumberColumn {

  def * = (
    label,
    abbreviation,
    number,
    lastModified,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: ((String, String, Int, Timestamp, UUID)) => FacultyDbEntry = {
    case (label, abbreviation, number, lastModified, id) =>
      FacultyDbEntry(label, abbreviation, number, lastModified, id)
  }

  def unmapRow
      : FacultyDbEntry => Option[(String, String, Int, Timestamp, UUID)] =
    a => Option((a.label, a.abbreviation, a.number, a.lastModified, a.id))
}
