package models

import database.tables.ModuleDbEntry
import play.api.libs.json.{Json, Writes}

import java.util.UUID

sealed trait Module extends UniqueEntity {
  def courseManagerId: UUID

  def label: String

  def abbreviation: String

  def credits: Double

  def descriptionUrl: String
}

object Module {
  implicit val writes: Writes[Module] = Writes.apply {
    case default: ModuleDefault => writesDefault.writes(default)
    case atom: ModuleAtom       => writesAtom.writes(atom)
  }

  implicit val writesDefault: Writes[ModuleDefault] = Json.writes[ModuleDefault]

  implicit val writesAtom: Writes[ModuleAtom] = Json.writes[ModuleAtom]

  case class ModuleDefault(
      courseManager: UUID,
      label: String,
      abbreviation: String,
      credits: Double,
      descriptionUrl: String,
      id: UUID
  ) extends Module {
    override def courseManagerId = courseManager
  }

  case class ModuleAtom(
      courseManager: User,
      label: String,
      abbreviation: String,
      credits: Double,
      descriptionUrl: String,
      id: UUID
  ) extends Module {
    override def courseManagerId = courseManager.id
  }

  def apply(db: ModuleDbEntry): ModuleDefault =
    ModuleDefault(
      db.courseManager,
      db.label,
      db.abbreviation,
      db.credits,
      db.descriptionUrl,
      db.id
    )
}
