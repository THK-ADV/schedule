package database.cols

import database.tables.ModuleTable
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

trait ModuleColumn {
  self: Table[_] =>
  def module = column[UUID]("module")

  def hasModule(id: UUID) = module === id

  def moduleFk =
    foreignKey("module", module, TableQuery[ModuleTable])(
      _.id,
      onUpdate = ForeignKeyAction.Restrict,
      onDelete = ForeignKeyAction.Restrict
    )
}
