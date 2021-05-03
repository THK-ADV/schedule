package database.cols

import database.tables.RoomTable
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

trait RoomColumn {
  self: Table[_] =>
  def room = column[UUID]("room")

  def hasRoom(id: UUID) = room === id

  def roomFk =
    foreignKey("room", room, TableQuery[RoomTable])(
      _.id,
      onUpdate = ForeignKeyAction.Restrict,
      onDelete = ForeignKeyAction.Restrict
    )
}
