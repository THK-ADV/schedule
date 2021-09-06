package models

import date.{LocalDateFormat, LocalTimeFormat}
import org.joda.time.{LocalDate, LocalTime}
import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class ScheduleWIPJson(
    course: UUID,
    room: UUID,
    moduleExaminationRegulation: UUID,
    date: LocalDate,
    start: LocalTime,
    end: LocalTime,
    notes: String,
    history: String,
    priority: Int
)

object ScheduleWIPJson extends LocalDateFormat with LocalTimeFormat {
  implicit val format: OFormat[ScheduleWIPJson] = Json.format[ScheduleWIPJson]
}
