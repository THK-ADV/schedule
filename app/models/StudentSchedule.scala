package models

import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class StudentSchedule(student: UUID, schedule: UUID, id: UUID)
    extends UniqueEntity

object StudentSchedule {
  implicit val format: OFormat[StudentSchedule] = Json.format[StudentSchedule]
}
