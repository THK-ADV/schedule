package database.tables

import database.UniqueDbEntry
import database.cols._
import models.Weekday
import slick.jdbc.PostgresProfile.api._

import java.sql.{Time, Timestamp}
import java.util.UUID

case class ScheduleWIPDbEntry(
    course: UUID,
    room: UUID,
    moduleExaminationRegulation: UUID,
    weekday: Weekday,
    start: Time,
    end: Time,
    notes: String,
    history: String,
    priority: Int,
    lastModified: Timestamp,
    id: UUID
) extends UniqueDbEntry

class ScheduleWIPTable(tag: Tag)
    extends Table[ScheduleWIPDbEntry](tag, "schedule_wip")
    with UniqueEntityColumn
    with CourseColumn
    with RoomColumn
    with StartEndTimeColumn
    with ModuleExaminationRegulationColumn {

  def notes = column[String]("notes")

  def weekday = column[Int]("weekday")

  def history = column[String]("history")

  def priority = column[Int]("priority")

  def * = (
    course,
    room,
    moduleExaminationRegulation,
    weekday,
    start,
    end,
    notes,
    history,
    priority,
    lastModified,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: (
      (UUID, UUID, UUID, Int, Time, Time, String, String, Int, Timestamp, UUID)
  ) => ScheduleWIPDbEntry = {
    case (
          course,
          room,
          moduleExaminationRegulation,
          date,
          start,
          end,
          notes,
          history,
          priority,
          lastModified,
          id
        ) =>
      ScheduleWIPDbEntry(
        course,
        room,
        moduleExaminationRegulation,
        Weekday(date),
        start,
        end,
        notes,
        history,
        priority,
        lastModified,
        id
      )
  }

  def unmapRow: ScheduleWIPDbEntry => Option[
    (
        UUID,
        UUID,
        UUID,
        Int,
        Time,
        Time,
        String,
        String,
        Int,
        Timestamp,
        UUID
    )
  ] = { a =>
    Option(
      (
        a.course,
        a.room,
        a.moduleExaminationRegulation,
        a.weekday.value,
        a.start,
        a.end,
        a.notes,
        a.history,
        a.priority,
        a.lastModified,
        a.id
      )
    )
  }
}
