package database.tables

import models.ModuleRelation
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

final class ModuleRelationTable(tag: Tag)
    extends Table[ModuleRelation](tag, "module_relation") {

  def parent = column[UUID]("parent", O.PrimaryKey)

  def child = column[UUID]("child", O.PrimaryKey)

  def * = (
    parent,
    child
  ) <> (ModuleRelation.tupled, ModuleRelation.unapply)
}
