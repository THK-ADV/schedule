package database
import org.joda.time.LocalDate
import slick.jdbc.PostgresProfile.api._

import java.sql.Date

trait StartEndTable extends SQLDateConverter { self: Table[_] =>
  def start = column[Date]("start")
  def end = column[Date]("end")

  def onStart(localDate: LocalDate): Rep[Boolean] =
    start === toSQLDate(localDate)

  def onEnd(localDate: LocalDate): Rep[Boolean] =
    end === toSQLDate(localDate)
}
