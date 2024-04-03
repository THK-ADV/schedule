import ops.DateOps
import org.joda.time.{LocalDate, LocalTime}
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
}
