package models

import database.tables.SubModuleDbEntry
import play.api.libs.json.{Json, Writes}

import java.util.UUID

sealed trait SubModule extends UniqueEntity {
  def moduleId: UUID

  def label: String

  def abbreviation: String

  def recommendedSemester: Int

  def credits: Double

  def descriptionUrl: String
}

object SubModule {
  implicit val writes: Writes[SubModule] = Writes.apply {
    case default: SubModuleDefault => writesDefault.writes(default)
    case atom: SubModuleAtom       => writesAtom.writes(atom)
  }

  implicit val writesDefault: Writes[SubModuleDefault] =
    Json.writes[SubModuleDefault]

  implicit val writesAtom: Writes[SubModuleAtom] = Json.writes[SubModuleAtom]

  case class SubModuleDefault(
      module: UUID,
      label: String,
      abbreviation: String,
      recommendedSemester: Int,
      credits: Double,
      descriptionUrl: String,
      id: UUID
  ) extends SubModule {
    override def moduleId = module
  }

  case class SubModuleAtom(
      module: Module,
      label: String,
      abbreviation: String,
      recommendedSemester: Int,
      credits: Double,
      descriptionUrl: String,
      id: UUID
  ) extends SubModule {
    override def moduleId = module.id
  }

  def apply(db: SubModuleDbEntry): SubModuleDefault =
    SubModuleDefault(
      db.module,
      db.label,
      db.abbreviation,
      db.recommendedSemester,
      db.credits,
      db.descriptionUrl,
      db.id
    )
}
