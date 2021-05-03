package database.tables

import database.cols.{
  AbbreviationColumn,
  IDColumn,
  LabelColumn,
  TeachingUnitColumn
}
import models.StudyProgram
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

class StudyProgramTable(tag: Tag)
    extends Table[StudyProgram](tag, "study_program")
    with IDColumn
    with AbbreviationColumn
    with LabelColumn
    with TeachingUnitColumn {

  def graduation = column[String]("graduation")

  def hasGraduation(g: String) = graduation === g

  def * = (
    teachingUnit,
    label,
    abbreviation,
    graduation,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: ((UUID, String, String, String, UUID)) => StudyProgram = {
    case (teachingUnit, label, abbreviation, graduation, id) =>
      StudyProgram(teachingUnit, label, abbreviation, graduation, id)
  }

  def unmapRow: StudyProgram => Option[(UUID, String, String, String, UUID)] =
    a => Option((a.teachingUnit, a.label, a.abbreviation, a.graduation, a.id))
}
