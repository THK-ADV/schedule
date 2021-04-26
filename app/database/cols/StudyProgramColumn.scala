package database.cols

import slick.jdbc.PostgresProfile.api._

import java.util.UUID

trait StudyProgramColumn {
  self: Table[_] =>
  def studyProgram = column[UUID]("study_program")

  def hasStudyProgram(id: UUID): Rep[Boolean] =
    studyProgram === id
}
