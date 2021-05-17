package database.cols

import database.tables.FacultyTable
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

trait FacultyColumn {
  self: Table[_] =>
  def faculty = column[UUID]("faculty")

  def hasFaculty(id: UUID) = faculty === id

  def facultyFk =
    foreignKey("faculty", faculty, TableQuery[FacultyTable])(
      _.id,
      onUpdate = ForeignKeyAction.Restrict,
      onDelete = ForeignKeyAction.Restrict
    )
}
