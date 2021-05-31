package database.cols

import database.tables.RoomTable
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

trait RoomColumn {
  self: Table[_] =>
  def room = column[UUID]("room")

  def room(id: UUID): Rep[Boolean] = room === id

  def label(label: String): Rep[Boolean] =
    roomFk.filter(_.hasLabel(label)).exists

  def abbrev(abbrev: String): Rep[Boolean] =
    roomFk.filter(_.hasAbbreviation(abbrev)).exists

  def campusQuery = roomFk.flatMap(_.campusFk)

  def campus(id: UUID) = campusQuery.filter(_.id === id).exists

  def campusLabel(label: String): Rep[Boolean] =
    campusQuery.filter(_.hasLabel(label)).exists

  def campusAbbrev(label: String): Rep[Boolean] =
    campusQuery.filter(_.hasLabel(label)).exists

  def roomFk =
    foreignKey("room", room, TableQuery[RoomTable])(
      _.id,
      onUpdate = ForeignKeyAction.Restrict,
      onDelete = ForeignKeyAction.Restrict
    )
}
