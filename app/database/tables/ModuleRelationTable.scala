package database.tables

import models.ModuleRelation
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

final class ModuleRelationTable(tag: Tag)
    extends Table[ModuleRelation](tag, "module_relation") {

  def parent = column[UUID]("parent", O.PrimaryKey)

  def child = column[UUID]("child", O.PrimaryKey)

  def parentFk =
    foreignKey("parent", parent, TableQuery[ModuleTable])(
      _.id,
      onUpdate = ForeignKeyAction.Restrict,
      onDelete = ForeignKeyAction.Restrict
    )

  def childFk =
    foreignKey("child", child, TableQuery[ModuleTable])(
      _.id,
      onUpdate = ForeignKeyAction.Restrict,
      onDelete = ForeignKeyAction.Restrict
    )

  def * = (
    parent,
    child
  ) <> (ModuleRelation.tupled, ModuleRelation.unapply)
}
