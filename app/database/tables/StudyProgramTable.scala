package database.tables

import database.cols.{AbbreviationColumn, IDColumn, LabelColumn}
import models.StudyProgram
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

class StudyProgramTable(tag: Tag)
    extends Table[StudyProgram](tag, "study_program")
    with IDColumn
    with AbbreviationColumn
    with LabelColumn {

  def graduation = column[String]("graduation")

  def hasGraduation(g: String) = graduation === g

  def * = (
    label,
    abbreviation,
    graduation,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: ((String, String, String, UUID)) => StudyProgram = {
    case (label, abbreviation, graduation, id) =>
      StudyProgram(label, abbreviation, graduation, id)
  }

  def unmapRow: StudyProgram => Option[(String, String, String, UUID)] = a =>
    Option((a.label, a.abbreviation, a.graduation, a.id))
}
