package database.tables

import models.ModuleSupervisor
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

final class ModuleSupervisorTable(tag: Tag)
    extends Table[ModuleSupervisor](tag, "module_supervisor") {

  def module = column[UUID]("module", O.PrimaryKey)

  def supervisor = column[String]("supervisor", O.PrimaryKey)

  def moduleFk =
    foreignKey("module", module, TableQuery[ModuleTable])(
      _.id,
      onUpdate = ForeignKeyAction.Restrict,
      onDelete = ForeignKeyAction.Restrict
    )

  def supervisorFk =
    foreignKey("supervisor", supervisor, TableQuery[PersonTable])(
      _.id,
      onUpdate = ForeignKeyAction.Restrict,
      onDelete = ForeignKeyAction.Restrict
    )

  def * = (
    module,
    supervisor
  ) <> (ModuleSupervisor.tupled, ModuleSupervisor.unapply)
}
