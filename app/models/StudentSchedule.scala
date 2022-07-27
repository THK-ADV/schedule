package models

import database.tables.StudentScheduleDbEntry

import java.util.UUID

sealed trait StudentSchedule extends UniqueEntity {
  def studentId: UUID

  def scheduleId: UUID
}

object StudentSchedule {
  case class StudentScheduleDefault(student: UUID, schedule: UUID, id: UUID)
      extends StudentSchedule {
    override def studentId = student

    override def scheduleId = schedule
  }

  case class StudentScheduleAtom(student: User, schedule: Schedule, id: UUID)
      extends StudentSchedule {
    override def studentId = student.id

    override def scheduleId = schedule.id
  }

  def apply(db: StudentScheduleDbEntry): StudentScheduleDefault =
    StudentScheduleDefault(db.student, db.schedule, db.id)
}
