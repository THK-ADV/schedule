package json

import models.ScheduleEntryStatus
import play.api.libs.json.Format

trait ScheduleEntryStatusFormat {
  implicit val scheduleEntryStatusFmt: Format[ScheduleEntryStatus] =
    Format
      .of[String]
      .bimap(ScheduleEntryStatus.apply, ScheduleEntryStatus.unapply)
}
