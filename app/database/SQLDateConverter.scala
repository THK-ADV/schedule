package database

import org.joda.time.{LocalDate, LocalTime}

import java.sql.{Date, Time}

trait SQLDateConverter {
  import date.DateFormatPattern.{dateFormatter, timeFormatter}

  implicit def toLocalDate(date: Date): LocalDate = new LocalDate(date.getTime)

  implicit def toLocalTime(time: Time): LocalTime = new LocalTime(time.getTime)

  implicit def toSQLDate(date: LocalDate): Date =
    Date.valueOf(date.toString(dateFormatter))

  implicit def toSQLTime(time: LocalTime): Time =
    Time.valueOf(time.toString(timeFormatter))
}
