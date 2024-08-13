package database.tables

import database.UUIDUniqueColumn
import database.tables.localDateTimeColumnType
import models.{SemesterPlanEntry, SemesterPlanEntryType}
import slick.jdbc.PostgresProfile.api._

import java.time.LocalDateTime
import java.util.UUID

final class SemesterPlanEntryTable(tag: Tag)
    extends Table[SemesterPlanEntry](tag, "semester_plan_entry")
    with UUIDUniqueColumn {

  def start = column[LocalDateTime]("start")(localDateTimeColumnType)

  def end = column[LocalDateTime]("end")(localDateTimeColumnType)

  def entryType =
    column[SemesterPlanEntryType]("type")(semesterPlanEntryTypeColumnType)

  def semester = column[UUID]("semester")

  def teachingUnit = column[Option[UUID]]("teaching_unit")

  def semesterIndex = column[Option[String]]("semester_index")

  def * = (
    id,
    start,
    end,
    entryType,
    semester,
    teachingUnit,
    semesterIndex
  ) <> ((SemesterPlanEntry.apply _).tupled, SemesterPlanEntry.unapply)
}
