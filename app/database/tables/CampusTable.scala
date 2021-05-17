package database.tables

import database.UniqueDbEntry
import database.cols.{AbbreviationColumn, LabelColumn, UniqueEntityColumn}
import slick.jdbc.PostgresProfile.api._

import java.sql.Timestamp
import java.util.UUID

case class CampusDbEntry(
    label: String,
    abbreviation: String,
    lastModified: Timestamp,
    id: UUID
) extends UniqueDbEntry

class CampusTable(tag: Tag)
    extends Table[CampusDbEntry](tag, "campus")
    with UniqueEntityColumn
    with LabelColumn
    with AbbreviationColumn {

  def * = (
    label,
    abbreviation,
    lastModified,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: ((String, String, Timestamp, UUID)) => CampusDbEntry = {
    case (label, abbreviation, lastModified, id) =>
      CampusDbEntry(label, abbreviation, lastModified, id)
  }

  def unmapRow: CampusDbEntry => Option[(String, String, Timestamp, UUID)] = {
    a =>
      Option((a.label, a.abbreviation, a.lastModified, a.id))
  }
}
