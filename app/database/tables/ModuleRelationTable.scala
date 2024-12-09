package database.tables

import java.util.UUID

import models.ModuleRelation
import slick.jdbc.PostgresProfile.api._

final class ModuleRelationTable(tag: Tag) extends Table[ModuleRelation](tag, "module_relation") {

  def parent = column[UUID]("parent", O.PrimaryKey)

  def child = column[UUID]("child", O.PrimaryKey)

  def * = (
    parent,
    child
  ) <> (ModuleRelation.apply, ModuleRelation.unapply)
}
