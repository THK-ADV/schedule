package models

import database.tables.RoomDbEntry

import java.util.UUID

sealed trait Room extends UniqueEntity {
  def campusId: UUID

  def label: String

  def abbreviation: String
}

object Room {
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
