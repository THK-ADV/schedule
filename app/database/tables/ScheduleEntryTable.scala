package database.tables

import database.UUIDUniqueColumn
import database.tables.localDateTimeColumnType
import models.ScheduleEntry
import slick.jdbc.PostgresProfile.api._

import java.time.LocalDateTime
import java.util.UUID

final class ScheduleEntryTable(tag: Tag)
    extends Table[ScheduleEntry](tag, "schedule_entry")
    with UUIDUniqueColumn {

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
