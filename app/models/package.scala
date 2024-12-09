import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

import ops.DateOps
import play.api.libs.json.Format

package object models {
  implicit def localDateFmt: Format[LocalDate] =
    Format
      .of[String]
      .bimap[LocalDate](DateOps.parseDate, DateOps.print)

  implicit def localTimeFmt: Format[LocalTime] =
    Format
      .of[String]
      .bimap[LocalTime](DateOps.parseTime, DateOps.print)

  implicit def localDateTimeFmt: Format[LocalDateTime] =
    Format
      .of[String]
      .bimap[LocalDateTime](DateOps.parseDateTime, DateOps.print)
}
