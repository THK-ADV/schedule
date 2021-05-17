package models

import database.tables.RoomDbEntry
import play.api.libs.json.{Json, Writes}

import java.util.UUID

sealed trait Room extends UniqueEntity {
  def campusId: UUID

  def label: String

  def abbreviation: String
}

object Room {
  implicit val writes: Writes[Room] = Writes.apply {
    case default: RoomDefault => writesDefault.writes(default)
    case atom: RoomAtom       => writesAtom.writes(atom)
  }

  implicit val writesDefault: Writes[RoomDefault] = Json.writes[RoomDefault]

  implicit val writesAtom: Writes[RoomAtom] = Json.writes[RoomAtom]

  case class RoomDefault(
      campus: UUID,
      label: String,
      abbreviation: String,
      id: UUID
  ) extends Room {
    override def campusId = campus
  }

  case class RoomAtom(
      campus: Campus,
      label: String,
      abbreviation: String,
      id: UUID
  ) extends Room {
    override def campusId = campus.id
  }

  def apply(db: RoomDbEntry): RoomDefault =
    RoomDefault(db.campus, db.label, db.abbreviation, db.id)
}
