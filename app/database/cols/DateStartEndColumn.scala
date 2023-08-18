//package database.cols
//
//import slick.jdbc.PostgresProfile.api._
//
//import java.sql.{Date, Time}
//
//trait DateStartEndColumn {
//  self: Table[_] =>
//
//  def date = column[Date]("date")
//
//  def start = column[Time]("start")
//
//  def end = column[Time]("end")
//
//  def onStart(time: Time): Rep[Boolean] =
//    this.start === time
//
//  def onEnd(time: Time): Rep[Boolean] =
//    this.end === time
//
//  def onDate(date: Date): Rep[Boolean] =
//    this.date === date
//
//  def sinceStart(start: Time): Rep[Boolean] =
//    this.start >= start
//
//  def untilEnd(end: Time): Rep[Boolean] =
//    this.end <= end
//
//  def sinceDate(date: Date): Rep[Boolean] =
//    this.date >= date
//
//  def untilDate(date: Date): Rep[Boolean] =
//    this.date <= date
//}
