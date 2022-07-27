package service

import database.repos.TeachingUnitRepository
import database.tables.{TeachingUnitDbEntry, TeachingUnitTable}
import models.{TeachingUnit, TeachingUnitJson}
import service.abstracts.Service

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class TeachingUnitService @Inject() (val repo: TeachingUnitRepository)
    extends Service[
      TeachingUnitJson,
      TeachingUnit,
      TeachingUnitDbEntry,
      TeachingUnitTable
    ] {

  override protected def toUniqueDbEntry(
      json: TeachingUnitJson,
      id: Option[UUID]
  ) =
    TeachingUnitDbEntry(
      json.faculty,
      json.label,
      json.abbreviation,
      json.number,
      now(),
      id getOrElse UUID.randomUUID
    )

  override protected def canUpdate(
      json: TeachingUnitJson,
      existing: TeachingUnitDbEntry
  ): Boolean =
    json.faculty == existing.faculty && json.number == existing.number

  override protected def uniqueCols(json: TeachingUnitJson) =
    List(_.hasFaculty(json.faculty), _.hasNumber(json.number))
}
