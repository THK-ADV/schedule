package database.tables

import database.cols.UUIDUniqueColumn
import models.Reservation
import org.joda.time.{LocalDate, LocalTime}
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

final class ReservationTable(tag: Tag)
    extends Table[Reservation](tag, "reservation")
    with UUIDUniqueColumn {

  import database.tables.{localDateColumnType, localTimeColumnType}

  def room = column[UUID]("room")

  def date = column[LocalDate]("date")

  def start = column[LocalTime]("start")

  def end = column[LocalTime]("end")

  def source = column[String]("source")

  def description = column[String]("description")

  def * = (
    id,
    room,
    date,
    start,
    end,
    source,
    description
  ) <> (Reservation.tupled, Reservation.unapply)
}
