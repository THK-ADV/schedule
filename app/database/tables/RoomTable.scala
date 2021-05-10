package database.tables

import database.cols.{AbbreviationColumn, UniqueEntityColumn, LabelColumn}
import models.Room
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

class RoomTable(tag: Tag)
    extends Table[Room](tag, "room")
    with UniqueEntityColumn
    with LabelColumn
    with AbbreviationColumn {

  def * = (
    label,
    abbreviation,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: ((String, String, UUID)) => Room = {
    case (label, abbreviation, id) =>
      Room(label, abbreviation, id)
  }

  def unmapRow: Room => Option[(String, String, UUID)] = { a =>
    Option((a.label, a.abbreviation, a.id))
  }
}
