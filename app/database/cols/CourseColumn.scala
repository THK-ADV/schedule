package database.cols

import database.tables.CourseTable
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

trait CourseColumn {
  self: Table[_] =>
  def course = column[UUID]("course")

  def hasCourse(id: UUID) = course === id

  def courseFk =
    foreignKey("course", course, TableQuery[CourseTable])(
      _.id,
      onUpdate = ForeignKeyAction.Restrict,
      onDelete = ForeignKeyAction.Restrict
    )
}
