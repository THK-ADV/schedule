package ops

import java.time.format.{DateTimeFormatter, DateTimeFormatterBuilder}
import java.time.temporal.ChronoField
import java.time.{LocalDate, LocalDateTime, LocalTime}

object DateOps {
  val datePattern = "yyyy-MM-dd"
  val dateFormatter = DateTimeFormatter.ofPattern(datePattern)
  val timePattern = "HH:mm"
  val timeFormatter = DateTimeFormatter.ofPattern(timePattern)

  val dateTimeFormatter = new DateTimeFormatterBuilder()
    .appendPattern(datePattern)
    .optionalStart()
    .appendPattern(s" $timePattern")
    .optionalEnd()
    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
    .toFormatter()

  def parseDate(string: String): LocalDate =
    LocalDate.parse(string, dateFormatter)

  def parseTime(string: String): LocalTime =
    LocalTime.parse(string, timeFormatter)

  // Parsing without formatting makes the time part optional
  def parseDateTime(string: String): LocalDateTime = {
    LocalDateTime.parse(string, dateTimeFormatter)
  }

  def print(localDate: LocalDate): String =
    localDate.format(dateFormatter)

  def print(localTime: LocalTime): String =
    localTime.format(timeFormatter)

  def print(localDateTime: LocalDateTime): String =
    localDateTime.format(dateTimeFormatter)
}
