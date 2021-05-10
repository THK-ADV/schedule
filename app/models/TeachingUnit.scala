package models

import database.tables.TeachingUnitDbEntry
import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class TeachingUnit(
    label: String,
    abbreviation: String,
    number: Int,
    id: UUID
) extends UniqueEntity

object TeachingUnit {
  implicit val format: OFormat[TeachingUnit] = Json.format[TeachingUnit]

  def apply(db: TeachingUnitDbEntry): TeachingUnit =
    TeachingUnit(db.label, db.abbreviation, db.number, db.id)
}
