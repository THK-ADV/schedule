package database.cols

import database.tables.CampusTable
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

trait CampusColumn {
  self: Table[_] =>
  def campus = column[UUID]("campus")

  def hasCampus(id: UUID) = campus === id

  def campusFk =
    foreignKey("campus", campus, TableQuery[CampusTable])(
      _.id,
      onUpdate = ForeignKeyAction.Restrict,
      onDelete = ForeignKeyAction.Restrict
    )
}
