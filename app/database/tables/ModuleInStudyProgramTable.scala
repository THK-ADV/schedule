package database.tables

import database.UUIDUniqueColumn
import models.ModuleInStudyProgram
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

final class ModuleInStudyProgramTable(tag: Tag)
    extends Table[ModuleInStudyProgram](tag, "module_in_study_program")
    with UUIDUniqueColumn {

  def module = column[UUID]("module")

  def studyProgram = column[UUID]("study_program")

  def mandatory = column[Boolean]("mandatory")

  def focus = column[Boolean]("focus")

  def recommendedSemester = column[List[Int]]("recommended_semester")

  def * = (
    id,
    module,
    studyProgram,
    mandatory,
    focus,
    recommendedSemester
  ) <> ((ModuleInStudyProgram.apply _).tupled, ModuleInStudyProgram.unapply)
}
