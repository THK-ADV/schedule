package database.tables

import database.UniqueDbEntry
import database.cols.{AbbreviationColumn, LabelColumn, UniqueEntityColumn}
import slick.jdbc.PostgresProfile.api._

import java.sql.Timestamp
import java.util.UUID

case class GraduationDbEntry(
    label: String,
    abbreviation: String,
    lastModified: Timestamp,
    id: UUID
) extends UniqueDbEntry

class GraduationTable(tag: Tag)
    extends Table[GraduationDbEntry](tag, "graduation")
    with UniqueEntityColumn
    with AbbreviationColumn
    with LabelColumn {

  def * = (
    label,
    abbreviation,
    lastModified,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: ((String, String, Timestamp, UUID)) => GraduationDbEntry = {
    case (label, abbreviation, lastModified, id) =>
      GraduationDbEntry(label, abbreviation, lastModified, id)
  }

  def unmapRow: GraduationDbEntry => Option[(String, String, Timestamp, UUID)] =
    a => Option((a.label, a.abbreviation, a.lastModified, a.id))
}
