package service

import database.repos.SubModuleRepository
import database.tables.SubModuleTable
import models.{SubModule, SubModuleJson}
import service.abstracts.Service

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class SubModuleService @Inject() (val repo: SubModuleRepository)
    extends Service[SubModuleJson, SubModule, SubModuleTable] {

  override protected def toModel(json: SubModuleJson, id: Option[UUID]) =
    SubModule(
      json.module,
      json.label,
      json.abbreviation,
      json.mandatory,
      json.recommendedSemester,
      json.credits,
      json.descriptionUrl,
      id getOrElse UUID.randomUUID
    )

  override protected def canUpdate(
      json: SubModuleJson,
      existing: SubModule
  ): Boolean =
    json.label == existing.label && json.module == existing.module

  override protected def uniqueCols(
      json: SubModuleJson,
      table: SubModuleTable
  ) =
    List(
      table.hasLabel(json.label),
      table.hasModule(json.module)
    )

  override protected def validate(json: SubModuleJson) = None
}
