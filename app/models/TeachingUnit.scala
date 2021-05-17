package models

import database.tables.TeachingUnitDbEntry
import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class TeachingUnit(
    faculty: UUID,
    label: String,
    abbreviation: String,
    number: Int,
    id: UUID
) extends UniqueEntity

object TeachingUnit {
  implicit val format: OFormat[TeachingUnit] = Json.format[TeachingUnit]

  def apply(db: TeachingUnitDbEntry): TeachingUnit =
    TeachingUnit(db.faculty, db.label, db.abbreviation, db.number, db.id)
}
