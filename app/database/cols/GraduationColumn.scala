package database.cols

import database.tables.GraduationTable
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

trait GraduationColumn {
  self: Table[_] =>
  def graduation = column[UUID]("graduation")

  def hasGraduation(id: UUID) = graduation === id

  def graduationFk =
    foreignKey("graduation", graduation, TableQuery[GraduationTable])(
      _.id,
      onUpdate = ForeignKeyAction.Restrict,
      onDelete = ForeignKeyAction.Restrict
    )
}
