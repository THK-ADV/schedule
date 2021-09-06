package database.cols

import slick.jdbc.PostgresProfile.api._

import java.sql.Date

trait DateStartEndColumn extends StartEndTimeColumn {
  self: Table[_] =>

  def date = column[Date]("date")

  def onDate(date: Date): Rep[Boolean] =
    this.date === date

  def sinceDate(date: Date): Rep[Boolean] =
    this.date >= date

  def untilDate(date: Date): Rep[Boolean] =
    this.date <= date
}
