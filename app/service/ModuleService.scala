package service

import database.repos.ModuleRepository
import database.tables.{ModuleDbEntry, ModuleTable}
import models.{Module, ModuleJson}
import service.abstracts.Service

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class ModuleService @Inject() (val repo: ModuleRepository)
    extends Service[ModuleJson, Module, ModuleDbEntry, ModuleTable] {

  override protected def toUniqueDbEntry(json: ModuleJson, id: Option[UUID]) =
    ModuleDbEntry(
      json.examinationRegulation,
      json.courseManager,
      json.label,
      json.abbreviation,
      json.credits,
      json.descriptionUrl,
      now(),
      id getOrElse UUID.randomUUID
    )

  override protected def canUpdate(
      json: ModuleJson,
      existing: ModuleDbEntry
  ): Boolean =
    json.label == existing.label && json.examinationRegulation == existing.examinationRegulation

  override protected def uniqueCols(json: ModuleJson, table: ModuleTable) =
    List(
      table.hasLabel(json.label),
      table.hasExaminationRegulation(json.examinationRegulation)
    )

  override protected def validate(json: ModuleJson) = None
}
