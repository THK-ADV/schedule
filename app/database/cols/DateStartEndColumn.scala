package database.cols

import database.SQLDateConverter
import org.joda.time.{LocalDate, LocalTime}
import slick.jdbc.PostgresProfile.api._

import java.sql.{Date, Time}

trait DateStartEndColumn extends SQLDateConverter {
  self: Table[_] =>

  def date = column[Date]("date")

  def start = column[Time]("start")

  def end = column[Time]("end")

  def onStart(localTime: LocalTime): Rep[Boolean] =
    start === toSQLTime(localTime)

  def onEnd(localTime: LocalTime): Rep[Boolean] =
    end === toSQLTime(localTime)

  def onDate(localDate: LocalDate): Rep[Boolean] =
    date === toSQLDate(localDate)
}
