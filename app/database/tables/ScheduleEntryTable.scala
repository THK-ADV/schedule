package database.tables

import java.time.LocalDateTime
import java.util.UUID

import database.tables.localDateTimeColumnType
import database.UUIDUniqueColumn
import models.ScheduleEntry
import slick.jdbc.PostgresProfile.api._

final class ScheduleEntryTable(tag: Tag) extends Table[ScheduleEntry](tag, "schedule_entry") with UUIDUniqueColumn {

  def course = column[UUID]("course")

  def start = column[LocalDateTime]("start")

  def end = column[LocalDateTime]("end")

  def * =
    (
      id,
      course,
      start,
      end
    ) <> (ScheduleEntry.apply, ScheduleEntry.unapply)
}
