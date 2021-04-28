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

  def hasStudyProgram(id: UUID): Rep[Boolean] =
    studyProgram === id
}
