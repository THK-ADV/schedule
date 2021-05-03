package database.tables

import database.cols.{CourseColumn, DateStartEndColumn, IDColumn, RoomColumn}
import models.Schedule
import slick.jdbc.PostgresProfile.api._

import java.sql.{Date, Time}
import java.util.UUID

class ScheduleTable(tag: Tag)
    extends Table[Schedule](tag, "schedule")
    with IDColumn
    with CourseColumn
    with RoomColumn
    with DateStartEndColumn {

  def * = (
    course,
    room,
    date,
    start,
    end,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: ((UUID, UUID, Date, Time, Time, UUID)) => Schedule = {
    case (course, room, date, start, end, id) =>
      Schedule(course, room, date, start, end, id)
  }

  def unmapRow: Schedule => Option[(UUID, UUID, Date, Time, Time, UUID)] = {
    a =>
      Option((a.course, a.room, a.date, a.start, a.end, a.id))
  }
}
