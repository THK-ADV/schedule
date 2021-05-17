package database.tables

import database.UniqueDbEntry
import database.cols.{
  AbbreviationColumn,
  CampusColumn,
  LabelColumn,
  UniqueEntityColumn
}
import slick.jdbc.PostgresProfile.api._

import java.sql.Timestamp
import java.util.UUID

case class RoomDbEntry(
    campus: UUID,
    label: String,
    abbreviation: String,
    lastModified: Timestamp,
    id: UUID
) extends UniqueDbEntry

class RoomTable(tag: Tag)
    extends Table[RoomDbEntry](tag, "room")
    with UniqueEntityColumn
    with LabelColumn
    with AbbreviationColumn
    with CampusColumn {

  def * = (
    campus,
    label,
    abbreviation,
    lastModified,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: ((UUID, String, String, Timestamp, UUID)) => RoomDbEntry = {
    case (campus, label, abbreviation, lastModified, id) =>
      RoomDbEntry(campus, label, abbreviation, lastModified, id)
  }

  def unmapRow
      : RoomDbEntry => Option[(UUID, String, String, Timestamp, UUID)] = { a =>
    Option((a.campus, a.label, a.abbreviation, a.lastModified, a.id))
  }
}
