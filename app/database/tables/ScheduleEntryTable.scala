package database.tables

import database.UUIDUniqueColumn
import models.ScheduleEntry
import org.joda.time.{LocalDate, LocalTime}
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

final class ScheduleEntryTable(tag: Tag)
    extends Table[ScheduleEntry](tag, "schedule_entry")
    with UUIDUniqueColumn {

  import database.tables.{localDateColumnType, localTimeColumnType}

  def course = column[UUID]("course")

  def date = column[LocalDate]("date")

  def start = column[LocalTime]("start")

  def end = column[LocalTime]("end")

  def * =
    (
      id,
      course,
      date,
      start,
      end
    ) <> (ScheduleEntry.tupled, ScheduleEntry.unapply)
}
