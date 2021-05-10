package models

import database.tables.GraduationDbEntry
import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class Graduation(label: String, abbreviation: String, id: UUID)
    extends UniqueEntity

object Graduation {
  implicit val format: OFormat[Graduation] = Json.format[Graduation]

  def apply(db: GraduationDbEntry): Graduation =
    Graduation(db.label, db.abbreviation, db.id)
}
