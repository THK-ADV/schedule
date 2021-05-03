package database.cols

import database.tables.ScheduleTable
import slick.jdbc.PostgresProfile.api._

import java.util.UUID

trait ScheduleColumn {
  self: Table[_] =>
  def schedule = column[UUID]("schedule")

  def scheduleFk =
    foreignKey("schedule", schedule, TableQuery[ScheduleTable])(
      _.id,
      onUpdate = ForeignKeyAction.Restrict,
      onDelete = ForeignKeyAction.Restrict
    )

  def hasSchedule(id: UUID): Rep[Boolean] = schedule === id
}
