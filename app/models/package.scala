import org.joda.time.format.DateTimeFormat
import org.joda.time.{LocalDate, LocalTime}
import play.api.libs.json.Format

package object models {
  val datePattern = "yyyy-MM-dd"
  val timePattern = "HH:mm:ss"

  val dateFormatter = DateTimeFormat.forPattern(datePattern)
  val timeFormatter = DateTimeFormat.forPattern(timePattern)

  implicit def localDateFmt: Format[LocalDate] =
    Format
      .of[String]
      .bimap[LocalDate](
        LocalDate.parse(_, dateFormatter),
        _.toString(dateFormatter)
      )

  implicit def localTimeFmt: Format[LocalTime] =
    Format
      .of[String]
      .bimap[LocalTime](
        LocalTime.parse(_, timeFormatter),
        _.toString(timeFormatter)
      )
}
