package models

import database.tables.StudentScheduleDbEntry
import play.api.libs.json.{Json, Writes}

import java.util.UUID

sealed trait StudentSchedule extends UniqueEntity {
  def studentId: UUID

  def scheduleId: UUID
}

object StudentSchedule {
  implicit val writes: Writes[StudentSchedule] = Writes.apply {
    case default: StudentScheduleDefault => writesDefault.writes(default)
    case atom: StudentScheduleAtom       => writesAtom.writes(atom)
  }

  implicit val writesDefault: Writes[StudentScheduleDefault] =
    Json.writes[StudentScheduleDefault]

  implicit val writesAtom: Writes[StudentScheduleAtom] =
    Json.writes[StudentScheduleAtom]

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
