package models

import database.tables.CampusDbEntry
import play.api.libs.json.{Json, OFormat}

import java.util.UUID

case class Campus(label: String, abbreviation: String, id: UUID)
    extends UniqueEntity

object Campus {
  implicit val format: OFormat[Campus] = Json.format[Campus]

  def apply(db: CampusDbEntry): Campus =
    Campus(db.label, db.abbreviation, db.id)
}
