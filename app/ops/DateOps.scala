package ops

import org.joda.time.format.DateTimeFormat
import org.joda.time.{LocalDate, LocalTime}

object DateOps {
  private val datePattern = "yyyy-MM-dd"
  private val dateFormatter = DateTimeFormat.forPattern(datePattern)
  private val timePattern = "HH:mm:ss"
  private val timeFormatter = DateTimeFormat.forPattern(timePattern)

  def parseDate(string: String): LocalDate =
    LocalDate.parse(string, dateFormatter)

  def parseTime(string: String): LocalTime =
    LocalTime.parse(string, timeFormatter)

  def print(localDate: LocalDate): String =
    localDate.toString(dateFormatter)

  def print(localTime: LocalTime): String =
    localTime.toString(timeFormatter)
}
