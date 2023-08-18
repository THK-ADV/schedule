package database.cols

import database.tables.GradeTable
import slick.jdbc.PostgresProfile.api._

trait GradeColumn {
  self: Table[_] =>
  def grade = column[String]("grade")

  def hasGrade(grade: String) = this.grade === grade

  def gradeFk =
    foreignKey("grade", grade, TableQuery[GradeTable])(
      _.id,
      onUpdate = ForeignKeyAction.Restrict,
      onDelete = ForeignKeyAction.Restrict
    )
}
