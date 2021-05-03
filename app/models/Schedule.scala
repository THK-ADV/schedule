package models

import date.{LocalDateFormat, LocalTimeFormat}
import org.joda.time.{LocalDate, LocalTime}
import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class Schedule(
    course: UUID,
    room: UUID,
    date: LocalDate,
    start: LocalTime,
    end: LocalTime,
    id: UUID
) extends UniqueEntity

object Schedule extends LocalDateFormat with LocalTimeFormat {
  implicit val format: OFormat[Schedule] = Json.format[Schedule]
}
