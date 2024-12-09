package database.tables

import java.util.UUID

import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape

case class ScheduleEntryRoom(scheduleEntry: UUID, room: UUID)

final class ScheduleEntryRoomTable(tag: Tag) extends Table[ScheduleEntryRoom](tag, "schedule_entry_room") {

  def scheduleEntry = column[UUID]("schedule_entry", O.PrimaryKey)

  def room = column[UUID]("room", O.PrimaryKey)

  override def * : ProvenShape[ScheduleEntryRoom] = (
    scheduleEntry,
    room
  ) <> (ScheduleEntryRoom.apply, ScheduleEntryRoom.unapply)
}
