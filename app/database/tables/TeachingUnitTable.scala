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

case class TeachingUnitDbEntry(
    label: String,
    abbreviation: String,
    number: Int,
    lastModified: Timestamp,
    id: UUID
) extends UniqueDbEntry

class TeachingUnitTable(tag: Tag)
    extends Table[TeachingUnitDbEntry](tag, "teaching_unit")
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

  def mapRow
      : ((String, String, Int, Timestamp, UUID)) => TeachingUnitDbEntry = {
    case (label, abbreviation, number, lastModified, id) =>
      TeachingUnitDbEntry(label, abbreviation, number, lastModified, id)
  }

  def unmapRow
      : TeachingUnitDbEntry => Option[(String, String, Int, Timestamp, UUID)] =
    a => Option((a.label, a.abbreviation, a.number, a.lastModified, a.id))
}
