package database.tables

import database.cols.UUIDUniqueColumn
import models.ModuleInStudyProgram
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

final class ModuleInStudyProgramTable(tag: Tag)
    extends Table[ModuleInStudyProgram](tag, "module_in_study_program")
    with UUIDUniqueColumn {

  def module = column[UUID]("module")

  def studyProgram = column[String]("study_program")

  def mandatory = column[Boolean]("mandatory")

  def recommendedSemester = column[List[Int]]("recommended_semester")

  def moduleFk =
    foreignKey("module", module, TableQuery[ModuleTable])(
      _.id,
      onUpdate = ForeignKeyAction.Restrict,
      onDelete = ForeignKeyAction.Restrict
    )

  def studyProgramFk =
    foreignKey("studyProgram", studyProgram, TableQuery[StudyProgramTable])(
      _.id,
      onUpdate = ForeignKeyAction.Restrict,
      onDelete = ForeignKeyAction.Restrict
    )

  def * = (
    id,
    module,
    studyProgram,
    mandatory,
    recommendedSemester
  ) <> (ModuleInStudyProgram.tupled, ModuleInStudyProgram.unapply)
}
