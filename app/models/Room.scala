package models

import java.util.UUID

case class Room(
    id: UUID,
    campus: UUID,
    label: String,
    identifier: String,
    roomType: String,
    capacity: Int
) extends UniqueEntity[UUID]
