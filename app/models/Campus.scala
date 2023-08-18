package models

import java.util.UUID

case class Campus(id: UUID, label: String, abbrev: String)
    extends UniqueEntity[UUID]
