package models

import database.tables._
import models.ExaminationRegulation.ExaminationRegulationAtom

import java.util.UUID

sealed trait ModuleExaminationRegulation extends UniqueEntity {
  def moduleId: UUID

  def examinationRegulationId: UUID

  def mandatory: Boolean
}

object ModuleExaminationRegulation {
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
