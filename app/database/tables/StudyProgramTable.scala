package database.tables

import database.cols._
import models.StudyProgram
import org.joda.time.LocalDate
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

final class StudyProgramTable(tag: Tag)
    extends Table[StudyProgram](tag, "study_program")
    with StringUniqueColumn
    with LocalizedLabelColumn {

  import database.tables.localDateColumnType

  def teachingUnit = column[UUID]("teaching_unit")

  def grade = column[String]("grade")

  def abbrev = column[String]("abbrev")

  def poNumber = column[Int]("po_number")

  def start = column[LocalDate]("start")

  def end = column[Option[LocalDate]]("end")

  def teachingUnitFk =
    foreignKey("teachingUnit", teachingUnit, TableQuery[TeachingUnitTable])(
      _.id,
      onUpdate = ForeignKeyAction.Restrict,
      onDelete = ForeignKeyAction.Restrict
    )

  def gradeFk =
    foreignKey("grade", grade, TableQuery[GradeTable])(
      _.id,
      onUpdate = ForeignKeyAction.Restrict,
      onDelete = ForeignKeyAction.Restrict
    )

  def * = (
    id,
    teachingUnit,
    grade,
    deLabel,
    enLabel,
    abbrev,
    poNumber,
    start,
    end
  ) <> (StudyProgram.tupled, StudyProgram.unapply)
}
