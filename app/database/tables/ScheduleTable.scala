package database.tables

import database.UniqueDbEntry
import database.cols.{
  CourseColumn,
  DateStartEndColumn,
  RoomColumn,
  UniqueEntityColumn
}
import slick.jdbc.PostgresProfile.api._

import java.sql.{Date, Time, Timestamp}
import java.util.UUID

case class ScheduleDbEntry(
    course: UUID,
    room: UUID,
    date: Date,
    start: Time,
    end: Time,
    lastModified: Timestamp,
    id: UUID
) extends UniqueDbEntry

class ScheduleTable(tag: Tag)
    extends Table[ScheduleDbEntry](tag, "schedule")
    with UniqueEntityColumn
    with CourseColumn
    with RoomColumn
    with DateStartEndColumn {

  def * = (
    course,
    room,
    date,
    start,
    end,
    lastModified,
    id
  ) <> (mapRow, unmapRow)

  def mapRow
      : ((UUID, UUID, Date, Time, Time, Timestamp, UUID)) => ScheduleDbEntry = {
    case (course, room, date, start, end, lastModified, id) =>
      ScheduleDbEntry(course, room, date, start, end, lastModified, id)
  }

  def unmapRow: ScheduleDbEntry => Option[
    (UUID, UUID, Date, Time, Time, Timestamp, UUID)
  ] = { a =>
    Option((a.course, a.room, a.date, a.start, a.end, a.lastModified, a.id))
  }
}
