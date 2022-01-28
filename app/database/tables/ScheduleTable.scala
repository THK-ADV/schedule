package database.tables

import database.UniqueDbEntry
import database.cols._
import models.ScheduleEntryStatus
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
    status: ScheduleEntryStatus,
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

  def status = column[String]("status")

  def hasStatus(s: ScheduleEntryStatus): Rep[Boolean] =
    this.status === s.toString

  def isDraft: Rep[Boolean] =
    this.hasStatus(ScheduleEntryStatus.Draft)

  def * = (
    course,
    room,
    moduleExaminationRegulation,
    date,
    start,
    end,
    status,
    lastModified,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: (
      (UUID, UUID, UUID, Date, Time, Time, String, Timestamp, UUID)
  ) => ScheduleDbEntry = {
    case (
          course,
          room,
          moduleExaminationRegulation,
          date,
          start,
          end,
          status,
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
        ScheduleEntryStatus(status),
        lastModified,
        id
      )
  }

  def unmapRow: ScheduleDbEntry => Option[
    (UUID, UUID, UUID, Date, Time, Time, String, Timestamp, UUID)
  ] = { a =>
    Option(
      (
        a.course,
        a.room,
        a.moduleExaminationRegulation,
        a.date,
        a.start,
        a.end,
        a.status.toString,
        a.lastModified,
        a.id
      )
    )
  }
}
