package database.cols

import database.tables.ModuleExaminationRegulationTable
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

trait ModuleExaminationRegulationColumn {
  self: Table[_] =>

  def moduleExaminationRegulation =
    column[UUID]("module_examination_regulation")

  def moduleExaminationRegulationFk =
    foreignKey(
      "moduleExaminationRegulation",
      moduleExaminationRegulation,
      TableQuery[ModuleExaminationRegulationTable]
    )(
      _.id,
      onUpdate = ForeignKeyAction.Restrict,
      onDelete = ForeignKeyAction.Restrict
    )

  def isModuleExaminationRegulation(id: UUID): Rep[Boolean] =
    moduleExaminationRegulation === id
}
