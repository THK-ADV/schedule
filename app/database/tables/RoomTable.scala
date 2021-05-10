package database.tables

import database.UniqueDbEntry
import database.cols.{AbbreviationColumn, LabelColumn, UniqueEntityColumn}
import slick.jdbc.PostgresProfile.api._

import java.sql.Timestamp
import java.util.UUID

case class RoomDbEntry(
    label: String,
    abbreviation: String,
    lastModified: Timestamp,
    id: UUID
) extends UniqueDbEntry

class RoomTable(tag: Tag)
    extends Table[RoomDbEntry](tag, "room")
    with UniqueEntityColumn
    with LabelColumn
    with AbbreviationColumn {

  def * = (
    label,
    abbreviation,
    lastModified,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: ((String, String, Timestamp, UUID)) => RoomDbEntry = {
    case (label, abbreviation, lastModified, id) =>
      RoomDbEntry(label, abbreviation, lastModified, id)
  }

  def unmapRow: RoomDbEntry => Option[(String, String, Timestamp, UUID)] = {
    a =>
      Option((a.label, a.abbreviation, a.lastModified, a.id))
  }
}
