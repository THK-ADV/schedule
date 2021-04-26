package database.tables

import database.cols.IDColumn
import models.StudyProgramAssociation
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

class StudyProgramAssociationTable(tag: Tag)
    extends Table[StudyProgramAssociation](tag, "study_program_association")
    with IDColumn {

  def teachingUnit = column[UUID]("teaching_unit")

  def studyProgram = column[UUID]("study_program")

  def * = (
    teachingUnit,
    studyProgram,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: ((UUID, UUID, UUID)) => StudyProgramAssociation = {
    case (teachingUnit, studyProgram, id) =>
      StudyProgramAssociation(teachingUnit, studyProgram, id)
  }

  def unmapRow: StudyProgramAssociation => Option[(UUID, UUID, UUID)] = a =>
    Option((a.teachingUnit, a.studyProgram, a.id))
}
