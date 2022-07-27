package models

import database.tables.GraduationDbEntry

import java.util.UUID

case class Graduation(label: String, abbreviation: String, id: UUID)
    extends UniqueEntity

object Graduation {
  def apply(db: GraduationDbEntry): Graduation =
    Graduation(db.label, db.abbreviation, db.id)
}
