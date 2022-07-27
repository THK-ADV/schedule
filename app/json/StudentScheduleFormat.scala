package json

import models.StudentSchedule.{StudentScheduleAtom, StudentScheduleDefault}
import models.{StudentSchedule, StudentScheduleJson}
import play.api.libs.json.{Json, OFormat, Writes}

trait StudentScheduleFormat { self: ScheduleFormat with UserFormat =>
  implicit val studentScheduleJsonFmt: OFormat[StudentScheduleJson] =
    Json.format[StudentScheduleJson]

  implicit val studentScheduleWrites: Writes[StudentSchedule] = Writes.apply {
    case default: StudentScheduleDefault =>
      studentScheduleDefaultWrites.writes(default)
    case atom: StudentScheduleAtom => studentScheduleAtomWrites.writes(atom)
  }

  implicit val studentScheduleDefaultWrites: Writes[StudentScheduleDefault] =
    Json.writes[StudentScheduleDefault]

  implicit val studentScheduleAtomWrites: Writes[StudentScheduleAtom] =
    Json.writes[StudentScheduleAtom]
}

object StudentScheduleFormat {
  trait All
      extends StudentScheduleFormat
      with ScheduleFormat.All
      with UserFormat
}
