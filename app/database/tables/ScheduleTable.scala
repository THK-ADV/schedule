package database.tables

import database.UniqueDbEntry
import database.cols._
import slick.jdbc.PostgresProfile.api._

import java.sql.{Date, Time, Timestamp}
import java.util.UUID

case class ScheduleDbEntry(
    course: UUID,
    room: UUID,
    moduleExaminationRegulation: UUID,
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
    with DateStartEndColumn
    with ModuleExaminationRegulationColumn {

  def * = (
    course,
    room,
    moduleExaminationRegulation,
    date,
    start,
    end,
    lastModified,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: (
      (UUID, UUID, UUID, Date, Time, Time, Timestamp, UUID)
  ) => ScheduleDbEntry = {
    case (
          course,
          room,
          moduleExaminationRegulation,
          date,
          start,
          end,
          lastModified,
          id
        ) =>
      ScheduleDbEntry(
        course,
        room,
        moduleExaminationRegulation,
        date,
        start,
        end,
        lastModified,
        id
      )
  }

  def unmapRow: ScheduleDbEntry => Option[
    (UUID, UUID, UUID, Date, Time, Time, Timestamp, UUID)
  ] = { a =>
    Option(
      (
        a.course,
        a.room,
        a.moduleExaminationRegulation,
        a.date,
        a.start,
        a.end,
        a.lastModified,
        a.id
      )
    )
  }
}
