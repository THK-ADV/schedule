package database.cols

import database.tables.StudyProgramTable
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

trait StudyProgramColumn {
  self: Table[_] =>
  def studyProgram = column[UUID]("study_program")

  def studyProgramFk =
    foreignKey("studyProgram", studyProgram, TableQuery[StudyProgramTable])(
      _.id,
      onUpdate = ForeignKeyAction.Restrict,
      onDelete = ForeignKeyAction.Restrict
    )

  def studyProgram(id: UUID): Rep[Boolean] =
    studyProgram === id

  def studyProgramLabel(label: String) =
    studyProgramFk.filter(_.hasLabel(label)).exists

  def studyProgramAbbreviation(abbrev: String) =
    studyProgramFk.filter(_.hasAbbreviation(abbrev)).exists

  def studyProgramGraduation(id: UUID) =
    studyProgramFk.filter(_.hasGraduation(id)).exists

  def studyProgramTeachingUnit(id: UUID) =
    studyProgramFk.filter(_.hasTeachingUnit(id)).exists
}
