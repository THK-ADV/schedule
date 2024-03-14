package database.tables

import database.UUIDUniqueColumn
import models.Room
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

final class RoomTable(tag: Tag)
    extends Table[Room](tag, "room")
    with UUIDUniqueColumn {

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
  ) <> ((Room.apply _).tupled, Room.unapply)
}
