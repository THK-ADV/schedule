package service

import database.repos.ModuleRepository
import database.tables.ModuleTable
import models.{Module, ModuleJson}
import service.abstracts.Service

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class ModuleService @Inject() (val repo: ModuleRepository)
    extends Service[ModuleJson, Module, ModuleTable] {

  override protected def toModel(json: ModuleJson, id: Option[UUID]) =
    Module(
      json.examinationRegulation,
      json.label,
      json.abbreviation,
      json.credits,
      json.descriptionUrl,
      id getOrElse UUID.randomUUID
    )

  override protected def canUpdate(
      json: ModuleJson,
      existing: Module
  ): Boolean =
    json.label == existing.label && json.examinationRegulation == existing.examinationRegulation

  override protected def uniqueCols(json: ModuleJson, table: ModuleTable) =
    List(
      table.hasLabel(json.label),
      table.hasExaminationRegulation(json.examinationRegulation)
    )

  override protected def validate(json: ModuleJson) = None
}
