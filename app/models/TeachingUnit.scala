package models

import database.tables.TeachingUnitDbEntry

import java.util.UUID

case class TeachingUnit(
    faculty: UUID,
    label: String,
    abbreviation: String,
    number: Int,
    id: UUID
) extends UniqueEntity

object TeachingUnit {
  def apply(db: TeachingUnitDbEntry): TeachingUnit =
    TeachingUnit(db.faculty, db.label, db.abbreviation, db.number, db.id)
}
