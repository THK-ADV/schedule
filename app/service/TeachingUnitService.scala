/*
package service

import database.tables.TeachingUnitTable
import models.{TeachingUnit, TeachingUnitJson}
import service.abstracts.Service

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class TeachingUnitService @Inject() (val repo: TeachingUnitRepository)
    extends Service[TeachingUnitJson, TeachingUnit, TeachingUnitTable] {

  override protected def toUniqueDbEntry(
      json: TeachingUnitJson,
      id: Option[UUID]
  ) =
    TeachingUnit(
      json.label,
      json.abbreviation,
      json.number,
      id getOrElse UUID.randomUUID
    )

  override protected def canUpdate(
      json: TeachingUnitJson,
      existing: TeachingUnit
  ): Boolean =
    json.label == existing.label && json.number == existing.number

  override protected def uniqueCols(
      json: TeachingUnitJson,
      table: TeachingUnitTable
  ) = List(table.hasLabel(json.label), table.hasNumber(json.number))

  override protected def validate(json: TeachingUnitJson) = None
}*/
