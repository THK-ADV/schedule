package database.tables

import database.cols.{IDColumn, ScheduleColumn, UserColumn}
import models.StudentSchedule
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

class StudentScheduleTable(tag: Tag)
    extends Table[StudentSchedule](tag, "student_schedule")
    with IDColumn
    with UserColumn
    with ScheduleColumn {

  override protected def userColumnName = "student"

  def * = (
    user,
    schedule,
    id
  ) <> (mapRow, unmapRow)

  def mapRow: ((UUID, UUID, UUID)) => StudentSchedule = {
    case (student, schedule, id) =>
      StudentSchedule(student, schedule, id)
  }

  def unmapRow: StudentSchedule => Option[(UUID, UUID, UUID)] = { a =>
    Option((a.student, a.schedule, a.id))
  }
}
