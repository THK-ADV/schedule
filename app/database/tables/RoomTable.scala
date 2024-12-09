package database.tables

import java.util.UUID

import database.UUIDUniqueColumn
import models.Room
import slick.jdbc.PostgresProfile.api._

final class RoomTable(tag: Tag) extends Table[Room](tag, "room") with UUIDUniqueColumn {

  def campus = column[UUID]("campus")

  def label = column[String]("label")

  def identifier = column[String]("identifier")

  def roomType = column[String]("type")

  def capacity = column[Int]("capacity")

  def * = (
    id,
    campus,
    label,
    identifier,
    roomType,
    capacity
  ) <> (Room.apply, Room.unapply)
}
