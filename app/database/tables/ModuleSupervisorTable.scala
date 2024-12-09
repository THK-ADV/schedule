package database.tables

import java.util.UUID

import models.ModuleSupervisor
import slick.jdbc.PostgresProfile.api._

final class ModuleSupervisorTable(tag: Tag) extends Table[ModuleSupervisor](tag, "module_supervisor") {

  def module = column[UUID]("module", O.PrimaryKey)

  def supervisor = column[String]("supervisor", O.PrimaryKey)

  def * = (
    module,
    supervisor
  ) <> (ModuleSupervisor.apply, ModuleSupervisor.unapply)
}
