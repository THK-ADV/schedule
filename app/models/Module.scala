package models

import play.api.libs.json.{Json, Writes}

import java.util.UUID

sealed trait Module extends UniqueEntity {
  def examinationRegulationId: UUID

  def courseManagerId: UUID

  def label: String

  def abbreviation: String

  def credits: Double

  def descriptionUrl: String
}

object Module {
  implicit val writes: Writes[Module] = Writes.apply {
    case default: ModuleDefault => writesDefault.writes(default)
    case atom: ModuleAtom       => wrtiesAtom.writes(atom)
  }

  implicit val writesDefault: Writes[ModuleDefault] = Json.writes[ModuleDefault]

  implicit val wrtiesAtom: Writes[ModuleAtom] = Json.writes[ModuleAtom]

  case class ModuleDefault(
      examinationRegulation: UUID,
      courseManager: UUID,
      label: String,
      abbreviation: String,
      credits: Double,
      descriptionUrl: String,
      id: UUID
  ) extends Module {
    override def examinationRegulationId = examinationRegulation

    override def courseManagerId = courseManager
  }

  case class ModuleAtom(
      examinationRegulation: ExaminationRegulation,
      courseManager: User,
      label: String,
      abbreviation: String,
      credits: Double,
      descriptionUrl: String,
      id: UUID
  ) extends Module {
    override def examinationRegulationId = examinationRegulation.id

    override def courseManagerId = courseManager.id
  }

}
