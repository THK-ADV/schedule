package database.tables

import database.cols.{IDColumn, LabelColumn}
import models.Room
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

class RoomTable(tag: Tag)
    extends Table[Room](tag, "room")
    with IDColumn
    with LabelColumn {

  def number = column[String]("number")

  def seats = column[Int]("seats")

  def roomType = column[String]("type")

  def hasNumber(number: String) = this.number === number

  def * = (
    label,
    number,
    seats,
    roomType,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: ((String, String, Int, String, UUID)) => Room = {
    case (label, number, seats, roomType, id) =>
      Room(label, number, seats, roomType, id)
  }

  def unmapRow: Room => Option[(String, String, Int, String, UUID)] = { a =>
    Option((a.label, a.number, a.seats, a.roomType, a.id))
  }
}
