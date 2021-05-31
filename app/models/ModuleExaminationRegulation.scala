package models

import database.tables.ModuleExaminationRegulationDbEntry
import models.ExaminationRegulation.ExaminationRegulationAtom
import play.api.libs.json.{Json, Writes}

import java.util.UUID

sealed trait ModuleExaminationRegulation extends UniqueEntity {
  def moduleId: UUID

  def examinationRegulationId: UUID

  def mandatory: Boolean
}

object ModuleExaminationRegulation {
  implicit val writes: Writes[ModuleExaminationRegulation] = Writes.apply {
    case default: ModuleExaminationRegulationDefault =>
      writesDefault.writes(default)
    case atom: ModuleExaminationRegulationAtom => writesAtom.writes(atom)
  }

  implicit val writesDefault: Writes[ModuleExaminationRegulationDefault] =
    Json.writes[ModuleExaminationRegulationDefault]

  implicit val writesAtom: Writes[ModuleExaminationRegulationAtom] =
    Json.writes[ModuleExaminationRegulationAtom]

  case class ModuleExaminationRegulationDefault(
      module: UUID,
      examinationRegulation: UUID,
      mandatory: Boolean,
      id: UUID
  ) extends ModuleExaminationRegulation {
    override def moduleId = module

    override def examinationRegulationId = examinationRegulation
  }

  case class ModuleExaminationRegulationAtom(
      module: Module,
      examinationRegulation: ExaminationRegulationAtom,
      mandatory: Boolean,
      id: UUID
  ) extends ModuleExaminationRegulation {
    override def moduleId = module.id

    override def examinationRegulationId = examinationRegulation.id
  }

  def apply(
      db: ModuleExaminationRegulationDbEntry
  ): ModuleExaminationRegulationDefault =
    ModuleExaminationRegulationDefault(
      db.module,
      db.examinationRegulation,
      db.mandatory,
      db.id
    )
}
