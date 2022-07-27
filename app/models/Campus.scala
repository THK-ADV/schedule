package models

import database.tables.CampusDbEntry

import java.util.UUID

case class Campus(label: String, abbreviation: String, id: UUID)
    extends UniqueEntity

object Campus {
  def apply(db: CampusDbEntry): Campus =
    Campus(db.label, db.abbreviation, db.id)
}
