package database.cols

import database.tables.SubModuleTable
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

trait SubModuleColumn {
  self: Table[_] =>
  def subModule = column[UUID]("submodule")

  def subModuleFk =
    foreignKey("submodule", subModule, TableQuery[SubModuleTable])(
      _.id,
      onUpdate = ForeignKeyAction.Restrict,
      onDelete = ForeignKeyAction.Restrict
    )

  def hasSubModule(id: UUID) = subModule === id
}
