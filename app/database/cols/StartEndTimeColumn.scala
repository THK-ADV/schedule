package database.cols

import slick.jdbc.PostgresProfile.api._

import java.sql.Time

trait StartEndTimeColumn {
  self: Table[_] =>

  def start = column[Time]("start")

  def end = column[Time]("end")

  def onStart(time: Time): Rep[Boolean] =
    this.start === time

  def onEnd(time: Time): Rep[Boolean] =
    this.end === time

  def sinceStart(start: Time): Rep[Boolean] =
    this.start >= start

  def untilEnd(end: Time): Rep[Boolean] =
    this.end <= end
}
