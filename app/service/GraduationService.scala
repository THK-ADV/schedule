package service

import database.repos.GraduationRepository
import database.tables.{GraduationDbEntry, GraduationTable}
import models.{Graduation, GraduationJson}
import service.abstracts.Service

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class GraduationService @Inject() (val repo: GraduationRepository)
    extends Service[
      GraduationJson,
      Graduation,
      GraduationDbEntry,
      GraduationTable
    ] {

  override protected def toUniqueDbEntry(
      json: GraduationJson,
      id: Option[UUID]
  ) =
    GraduationDbEntry(
      json.label,
      json.abbreviation,
      now(),
      id getOrElse UUID.randomUUID
    )

  override protected def canUpdate(
      json: GraduationJson,
      existing: GraduationDbEntry
  ): Boolean =
    json.label == existing.label && json.abbreviation == existing.abbreviation

  override protected def uniqueCols(json: GraduationJson) =
    List(
      _.hasLabel(json.label),
      _.hasAbbreviation(json.abbreviation)
    )
}
