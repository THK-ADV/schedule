package models

import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class StudentScheduleJson(student: UUID, schedule: UUID)

object StudentScheduleJson {
  implicit val format: OFormat[StudentScheduleJson] =
    Json.format[StudentScheduleJson]
}
