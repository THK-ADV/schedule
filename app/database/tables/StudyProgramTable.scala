package database.tables

import database.cols._
import models.StudyProgram
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

class StudyProgramTable(tag: Tag)
    extends Table[StudyProgram](tag, "study_program")
    with IDColumn
    with AbbreviationColumn
    with LabelColumn
    with TeachingUnitColumn
    with GraduationColumn {

  def * = (
    teachingUnit,
    graduation,
    label,
    abbreviation,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: ((UUID, UUID, String, String, UUID)) => StudyProgram = {
    case (teachingUnit, graduation, label, abbreviation, id) =>
      StudyProgram(teachingUnit, graduation, label, abbreviation, id)
  }

  def unmapRow: StudyProgram => Option[(UUID, UUID, String, String, UUID)] =
    a => Option((a.teachingUnit, a.graduation, a.label, a.abbreviation, a.id))
}
