package database.cols

import database.tables.ExaminationRegulationTable
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

trait ExaminationRegulationColumn {
  self: Table[_] =>
  def examinationRegulation = column[UUID]("examination_regulation")

  def hasExaminationRegulation(id: UUID) = examinationRegulation === id

  def examinationRegulationFk =
    foreignKey(
      "examinationRegulation",
      examinationRegulation,
      TableQuery[ExaminationRegulationTable]
    )(
      _.id,
      onUpdate = ForeignKeyAction.Restrict,
      onDelete = ForeignKeyAction.Restrict
    )
}
