package models

import java.util.UUID

case class ScheduleEntry(id: UUID, course: UUID, reservation: UUID)
    extends UniqueEntity[UUID]
