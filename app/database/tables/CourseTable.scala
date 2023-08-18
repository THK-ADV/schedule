package database.tables

import database.cols.UUIDUniqueColumn
import models.{Course, ModulePart}
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

final class CourseTable(tag: Tag)
    extends Table[Course](tag, "course")
    with UUIDUniqueColumn {

  def semester = column[UUID]("semester")

  def module = column[UUID]("module")

  def studyProgram = column[String]("study_program")

  def modulePart = column[ModulePart]("part")

  def semesterFk =
    foreignKey("semester", semester, TableQuery[SemesterTable])(
      _.id,
      onUpdate = ForeignKeyAction.Restrict,
      onDelete = ForeignKeyAction.Restrict
    )

  def moduleFk =
    foreignKey("module", module, TableQuery[ModuleTable])(
      _.id,
      onUpdate = ForeignKeyAction.Restrict,
      onDelete = ForeignKeyAction.Restrict
    )

  def studyProgramFk =
    foreignKey("study_program", studyProgram, TableQuery[StudyProgramTable])(
      _.id,
      onUpdate = ForeignKeyAction.Restrict,
      onDelete = ForeignKeyAction.Restrict
    )

  def * = (
    id,
    semester,
    module,
    studyProgram,
    modulePart
  ) <> (Course.tupled, Course.unapply)
}
