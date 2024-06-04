package ops

import org.joda.time.format.DateTimeFormat
import org.joda.time.{LocalDate, LocalDateTime, LocalTime}

object DateOps {
  private val datePattern = "yyyy-MM-dd"
  private val dateFormatter = DateTimeFormat.forPattern(datePattern)
  private val timePattern = "HH:mm:ss"
  private val timeFormatter = DateTimeFormat.forPattern(timePattern)
  private val dateTimePattern = "yyyy-MM-dd HH:mm:ss"
  private val dateTimeFormatter = DateTimeFormat.forPattern(dateTimePattern)

  def parseDate(string: String): LocalDate =
    LocalDate.parse(string, dateFormatter)

  def parseTime(string: String): LocalTime =
    LocalTime.parse(string, timeFormatter)

  // Parsing without formatting makes the time part optional
  def parseDateTime(string: String): LocalDateTime =
    LocalDateTime.parse(string)

  def print(localDate: LocalDate): String =
    localDate.toString(dateFormatter)

  def print(localTime: LocalTime): String =
    localTime.toString(timeFormatter)

  def print(localDateTime: LocalDateTime): String =
    localDateTime.toString(dateTimeFormatter)
}
