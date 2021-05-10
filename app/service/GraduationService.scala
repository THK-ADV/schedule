/*
package service

import database.tables.GraduationTable
import models.{Graduation, GraduationJson}
import service.abstracts.Service

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class GraduationService @Inject() (val repo: GraduationRepository)
    extends Service[GraduationJson, Graduation, GraduationTable] {

  override protected def toUniqueDbEntry(
      json: GraduationJson,
      id: Option[UUID]
  ) =
    Graduation(
      json.label,
      json.abbreviation,
      id getOrElse UUID.randomUUID
    )

  override protected def canUpdate(
      json: GraduationJson,
      existing: Graduation
  ): Boolean =
    json.label == existing.label && json.abbreviation == existing.abbreviation

  override protected def uniqueCols(
      json: GraduationJson,
      table: GraduationTable
  ) =
    List(
      table.hasLabel(json.label),
      table.hasAbbreviation(json.abbreviation)
    )

  override protected def validate(json: GraduationJson) = None
}*/
