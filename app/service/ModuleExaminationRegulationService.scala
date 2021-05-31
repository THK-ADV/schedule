package service

import database.repos.ModuleExaminationRegulationRepository
import database.tables.{
  ModuleExaminationRegulationDbEntry,
  ModuleExaminationRegulationTable
}
import models.{ModuleExaminationRegulation, ModuleExaminationRegulationJson}
import service.abstracts.Service

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class ModuleExaminationRegulationService @Inject() (
    val repo: ModuleExaminationRegulationRepository
) extends Service[
      ModuleExaminationRegulationJson,
      ModuleExaminationRegulation,
      ModuleExaminationRegulationDbEntry,
      ModuleExaminationRegulationTable
    ] {

  override protected def toUniqueDbEntry(
      json: ModuleExaminationRegulationJson,
      id: Option[UUID]
  ) =
    ModuleExaminationRegulationDbEntry(
      json.module,
      json.examinationRegulation,
      json.mandatory,
      now(),
      id getOrElse UUID.randomUUID
    )

  override protected def canUpdate(
      json: ModuleExaminationRegulationJson,
      existing: ModuleExaminationRegulationDbEntry
  ): Boolean = false

  override protected def uniqueCols(json: ModuleExaminationRegulationJson) =
    List(
      _.isModule(json.module),
      _.examinationRegulation(json.examinationRegulation),
      _.isMandatory(json.mandatory)
    )

  override protected def validate(json: ModuleExaminationRegulationJson) = None
}
