package service

import database.repos.SubModuleRepository
import database.tables.{SubModuleDbEntry, SubModuleTable}
import models.{SubModule, SubModuleJson}
import service.abstracts.Service

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class SubModuleService @Inject() (val repo: SubModuleRepository)
    extends Service[
      SubModuleJson,
      SubModule,
      SubModuleDbEntry,
      SubModuleTable
    ] {

  override protected def toUniqueDbEntry(
      json: SubModuleJson,
      id: Option[UUID]
  ) =
    SubModuleDbEntry(
      json.module,
      json.label,
      json.abbreviation,
      json.recommendedSemester,
      json.credits,
      json.descriptionUrl,
      json.language,
      json.season,
      now(),
      id getOrElse UUID.randomUUID
    )

  override protected def canUpdate(
      json: SubModuleJson,
      existing: SubModuleDbEntry
  ): Boolean =
    json.label == existing.label && json.module == existing.module

  override protected def uniqueCols(json: SubModuleJson) =
    List(
      _.hasLabel(json.label),
      _.hasModule(json.module)
    )

  override protected def validate(json: SubModuleJson) = None
}
