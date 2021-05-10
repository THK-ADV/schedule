package models

import database.SQLDateConverter
import database.tables.ScheduleDbEntry
import date.{LocalDateFormat, LocalTimeFormat}
import org.joda.time.{LocalDate, LocalTime}
import play.api.libs.json.{Json, Writes}

import java.util.UUID

sealed trait Schedule extends UniqueEntity {
  def courseId: UUID

  def roomId: UUID

  def date: LocalDate

  def start: LocalTime

  def end: LocalTime
}

object Schedule
    extends LocalDateFormat
    with LocalTimeFormat
    with SQLDateConverter {
  implicit val writes: Writes[Schedule] = Writes.apply {
    case default: ScheduleDefault => writesDefault.writes(default)
    case atom: ScheduleAtom       => writesAtom.writes(atom)
  }

  implicit val writesDefault: Writes[ScheduleDefault] =
    Json.writes[ScheduleDefault]

  implicit val writesAtom: Writes[ScheduleAtom] = Json.writes[ScheduleAtom]

  case class ScheduleDefault(
      course: UUID,
      room: UUID,
      date: LocalDate,
      start: LocalTime,
      end: LocalTime,
      id: UUID
  ) extends Schedule {
    override def courseId = course

    override def roomId = room
  }

  case class ScheduleAtom(
      course: Course,
      room: Room,
      date: LocalDate,
      start: LocalTime,
      end: LocalTime,
      id: UUID
  ) extends Schedule {
    override def courseId = course.id

    override def roomId = room.id
  }

  def apply(db: ScheduleDbEntry): ScheduleDefault =
    ScheduleDefault(db.course, db.room, db.date, db.start, db.end, db.id)
}
