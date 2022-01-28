package models

import date.{LocalDateFormat, LocalTimeFormat}
import org.joda.time.{LocalDate, LocalTime}
import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class ScheduleJson(
    course: UUID,
    room: UUID,
    moduleExaminationRegulation: UUID,
    date: LocalDate,
    start: LocalTime,
    end: LocalTime,
    status: ScheduleEntryStatus
)

object ScheduleJson extends LocalDateFormat with LocalTimeFormat {
  implicit val format: OFormat[ScheduleJson] = Json.format[ScheduleJson]
}
