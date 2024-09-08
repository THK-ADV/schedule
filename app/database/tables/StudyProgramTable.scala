package database.tables

import database.UUIDUniqueColumn
import models.StudyProgram
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

final class StudyProgramTable(tag: Tag)
    extends Table[StudyProgram](tag, "study_program")
    with UUIDUniqueColumn {

  def teachingUnit = column[UUID]("teaching_unit")

  def degree = column[String]("degree")

  def abbrev = column[String]("abbrev")

  def poNumber = column[Int]("po_number")

  def poId = column[String]("po_id")

  def specializationId = column[Option[String]]("specialization_id")

  def deLabel = column[String]("de_label")

  def enLabel = column[String]("en_label")
  
  def isPO(poId: String, specializationId: Option[String]) =
    this.poId === poId && this.specializationId === specializationId 

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
  ) <> ((StudyProgram.apply _).tupled, StudyProgram.unapply)
}
