package models

import java.util.UUID

case class TeachingUnitJson(
    faculty: UUID,
    label: String,
    abbreviation: String,
    number: Int
)
