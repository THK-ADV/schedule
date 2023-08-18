package database.cols

import database.tables.FacultyTable
import slick.jdbc.PostgresProfile.api._

trait FacultyColumn {
  self: Table[_] =>
  def faculty = column[String]("faculty")

  def hasFaculty(id: String) = faculty === id

  def facultyFk =
    foreignKey("faculty", faculty, TableQuery[FacultyTable])(
      _.id,
      onUpdate = ForeignKeyAction.Restrict,
      onDelete = ForeignKeyAction.Restrict
    )
}
