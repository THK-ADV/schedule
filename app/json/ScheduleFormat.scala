package json

import models.Schedule.{ScheduleAtom, ScheduleDefault}
import models.{Schedule, ScheduleJson}
import play.api.libs.json.{Json, OFormat, Writes}

trait ScheduleFormat {
  self: LocalDateFormat
    with LocalTimeFormat
    with CourseFormat
    with RoomFormat
    with ModuleExaminationRegulationFormat
    with ScheduleEntryStatusFormat =>

  implicit val scheduleJsonFmt: OFormat[ScheduleJson] =
    Json.format[ScheduleJson]

  implicit val scheduleWrites: Writes[Schedule] = Writes.apply {
    case default: ScheduleDefault => scheduleDefaultWrites.writes(default)
    case atom: ScheduleAtom       => scheduleAtomWrites.writes(atom)
  }

  implicit val scheduleDefaultWrites: Writes[ScheduleDefault] =
    Json.writes[ScheduleDefault]

  implicit val scheduleAtomWrites: Writes[ScheduleAtom] =
    Json.writes[ScheduleAtom]
}

object ScheduleFormat {
  trait All
      extends ScheduleFormat
      with LocalDateFormat
      with LocalTimeFormat
      with CourseFormat.All
      with RoomFormat.All
      with ModuleExaminationRegulationFormat.All
      with ScheduleEntryStatusFormat
}
