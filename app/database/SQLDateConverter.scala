package database

import org.joda.time.{LocalDate, LocalTime}

import java.sql.{Date, Time}
import scala.language.implicitConversions
import scala.util.Try

trait SQLDateConverter {

  import controllers.json.DateFormatPattern.{dateFormatter, timeFormatter}

  implicit def toLocalDate(date: Date): LocalDate = new LocalDate(date.getTime)

  implicit def toLocalTime(time: Time): LocalTime = new LocalTime(time.getTime)

  implicit def toSQLDate(date: LocalDate): Date =
    Date.valueOf(date.toString(dateFormatter))

  implicit def toSQLTime(time: LocalTime): Time =
    Time.valueOf(time.toString(timeFormatter))

  implicit def toSQLTime(str: String): Try[Time] =
    Try(timeFormatter.parseMillis(str))
      .map(millis => new Time(millis))

  implicit def toSQLDate(str: String): Try[Date] =
    Try(dateFormatter.parseMillis(str))
      .map(millis => new Date(millis))
}
