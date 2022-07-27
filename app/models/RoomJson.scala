package models

import java.util.UUID

case class RoomJson(
    campus: UUID,
    label: String,
    abbreviation: String
)
