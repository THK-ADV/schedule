package database.tables

import models.ModuleSupervisor
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

final class ModuleSupervisorTable(tag: Tag)
    extends Table[ModuleSupervisor](tag, "module_supervisor") {

  def module = column[UUID]("module", O.PrimaryKey)

  def supervisor = column[String]("supervisor", O.PrimaryKey)

  def * = (
    module,
    supervisor
  ) <> (ModuleSupervisor.apply, ModuleSupervisor.unapply)
}
