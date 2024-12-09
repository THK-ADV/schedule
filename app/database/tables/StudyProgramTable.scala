package database.tables

import java.util.UUID

import database.UUIDUniqueColumn
import models.StudyProgram
import slick.jdbc.PostgresProfile.api._

final class StudyProgramTable(tag: Tag) extends Table[StudyProgram](tag, "study_program") with UUIDUniqueColumn {

  def teachingUnit = column[UUID]("teaching_unit")

  def degree = column[String]("degree")

  def abbrev = column[String]("abbrev")

  def poNumber = column[Int]("po_number")

  def poId = column[String]("po_id")

  def specializationId = column[Option[String]]("specialization_id")

  def deLabel = column[String]("de_label")

  def enLabel = column[String]("en_label")

  def * = (
    id,
    teachingUnit,
    degree,
    deLabel,
    enLabel,
    abbrev,
    poId,
    poNumber,
    specializationId
  ) <> (StudyProgram.apply, StudyProgram.unapply)
}
