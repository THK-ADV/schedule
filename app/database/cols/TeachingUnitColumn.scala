package database.cols

import database.tables.TeachingUnitTable
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

trait TeachingUnitColumn {
  self: Table[_] =>
  def teachingUnit = column[UUID]("teaching_unit")

  def teachingUnitFk =
    foreignKey("teachingUnit", teachingUnit, TableQuery[TeachingUnitTable])(
      _.id,
      onUpdate = ForeignKeyAction.Restrict,
      onDelete = ForeignKeyAction.Restrict
    )

  def hasTeachingUnit(id: UUID): Rep[Boolean] =
    teachingUnit === id
}
