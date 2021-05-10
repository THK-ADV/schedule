package database.tables

import database.UniqueDbEntry
import database.cols.{ScheduleColumn, UniqueEntityColumn, UserColumn}
import slick.jdbc.PostgresProfile.api._

import java.sql.Timestamp
import java.util.UUID

case class StudentScheduleDbEntry(
    student: UUID,
    schedule: UUID,
    lastModified: Timestamp,
    id: UUID
) extends UniqueDbEntry

class StudentScheduleTable(tag: Tag)
    extends Table[StudentScheduleDbEntry](tag, "student_schedule")
    with UniqueEntityColumn
    with UserColumn
    with ScheduleColumn {

  override protected def userColumnName = "student"

  def * = (
    user,
    schedule,
    lastModified,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: ((UUID, UUID, Timestamp, UUID)) => StudentScheduleDbEntry = {
    case (student, schedule, lastModified, id) =>
      StudentScheduleDbEntry(student, schedule, lastModified, id)
  }

  def unmapRow
      : StudentScheduleDbEntry => Option[(UUID, UUID, Timestamp, UUID)] = { a =>
    Option((a.student, a.schedule, a.lastModified, a.id))
  }
}
