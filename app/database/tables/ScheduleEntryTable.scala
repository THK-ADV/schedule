package database.tables

import database.UUIDUniqueColumn
import models.ScheduleEntry
import org.joda.time.LocalDateTime
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

final class ScheduleEntryTable(tag: Tag)
    extends Table[ScheduleEntry](tag, "schedule_entry")
    with UUIDUniqueColumn {

  import database.tables.localDateTimeColumnType

  def course = column[UUID]("course")

  def start = column[LocalDateTime]("start")

  def end = column[LocalDateTime]("end")

  def * =
    (
      id,
      course,
      start,
      end
    ) <> (ScheduleEntry.tupled, ScheduleEntry.unapply)
}
