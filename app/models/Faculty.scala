package models

import java.util.UUID

case class Faculty(label: String, abbreviation: String, number: Int, id: UUID)
    extends UniqueEntity
