package database.tables

import database.UniqueDbEntry
import database.cols.{AbbreviationColumn, LabelColumn, UniqueEntityColumn}
import slick.jdbc.PostgresProfile.api._

import java.sql.Timestamp
import java.util.UUID

case class FacultyDbEntry(
    label: String,
    abbreviation: String,
    lastModified: Timestamp,
    id: UUID
) extends UniqueDbEntry

class FacultyTable(tag: Tag)
    extends Table[FacultyDbEntry](tag, "faculty")
    with UniqueEntityColumn
    with AbbreviationColumn
    with LabelColumn {

  def * = (
    label,
    abbreviation,
    lastModified,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: ((String, String, Timestamp, UUID)) => FacultyDbEntry = {
    case (label, abbreviation, lastModified, id) =>
      FacultyDbEntry(label, abbreviation, lastModified, id)
  }

  def unmapRow: FacultyDbEntry => Option[(String, String, Timestamp, UUID)] =
    a => Option((a.label, a.abbreviation, a.lastModified, a.id))
}
