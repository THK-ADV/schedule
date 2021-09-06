package models

import database.SQLDateConverter
import database.tables.ScheduleWIPDbEntry
import date.{LocalDateFormat, LocalTimeFormat}
import org.joda.time.{LocalDate, LocalTime}
import play.api.libs.json.{Json, Writes}

import java.util.UUID

trait ScheduleWIP extends UniqueEntity {
  def courseId: UUID
  def roomId: UUID
  def moduleExaminationRegulationId: UUID
  def date: LocalDate
  def start: LocalTime
  def end: LocalTime
  def notes: String
  def history: String
  def priority: Int
}

object ScheduleWIP
    extends LocalDateFormat
    with LocalTimeFormat
    with SQLDateConverter {
  implicit val writes: Writes[ScheduleWIP] = Writes.apply {
    case default: ScheduleWIPDefault => writesDefault.writes(default)
  }

  implicit val writesDefault: Writes[ScheduleWIPDefault] =
    Json.writes[ScheduleWIPDefault]

  case class ScheduleWIPDefault(
      courseId: UUID,
      roomId: UUID,
      moduleExaminationRegulationId: UUID,
      date: LocalDate,
      start: LocalTime,
      end: LocalTime,
      notes: String,
      history: String,
      priority: Int,
      id: UUID
  ) extends ScheduleWIP

  def apply(db: ScheduleWIPDbEntry): ScheduleWIPDefault =
    ScheduleWIPDefault(
      db.course,
      db.room,
      db.moduleExaminationRegulation,
      db.date,
      db.start,
      db.end,
      db.notes,
      db.history,
      db.priority,
      db.id
    )
}
