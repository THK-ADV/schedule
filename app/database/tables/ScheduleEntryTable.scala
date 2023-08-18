package database.tables

import database.cols._
import models.ScheduleEntry
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

final class ScheduleEntryTable(tag: Tag)
    extends Table[ScheduleEntry](tag, "schedule_entry")
    with UUIDUniqueColumn {

  def course = column[UUID]("course")

  def reservation = column[UUID]("reservation")

  def courseFk =
    foreignKey("course", course, TableQuery[CourseTable])(
      _.id,
      onUpdate = ForeignKeyAction.Restrict,
      onDelete = ForeignKeyAction.Restrict
    )

  def reservationFk =
    foreignKey("reservation", reservation, TableQuery[ReservationTable])(
      _.id,
      onUpdate = ForeignKeyAction.Restrict,
      onDelete = ForeignKeyAction.Restrict
    )

  def * =
    (id, course, reservation) <> (ScheduleEntry.tupled, ScheduleEntry.unapply)
}
