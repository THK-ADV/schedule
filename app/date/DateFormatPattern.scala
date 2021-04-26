package date

import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}

object DateFormatPattern {
  val dateTimePattern = "yyyy-MM-dd'T'HH:mm"
  val datePattern = "yyyy-MM-dd"
  val timePattern = "HH:mm:ss"

  val dateFormatter = DateTimeFormat.forPattern(datePattern)
  val timeFormatter = DateTimeFormat.forPattern(timePattern)
}
