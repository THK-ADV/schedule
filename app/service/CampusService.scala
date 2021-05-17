package service

import database.repos.CampusRepository
import database.tables
import database.tables.{CampusDbEntry, CampusTable}
import models.{Campus, CampusJson}
import service.abstracts.Service

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class CampusService @Inject() (val repo: CampusRepository)
    extends Service[CampusJson, Campus, CampusDbEntry, CampusTable] {

  override protected def toUniqueDbEntry(json: CampusJson, id: Option[UUID]) =
    tables.CampusDbEntry(
      json.label,
      json.abbreviation,
      now(),
      id getOrElse UUID.randomUUID
    )

  override protected def canUpdate(
      json: CampusJson,
      existing: CampusDbEntry
  ): Boolean = true

  override protected def uniqueCols(json: CampusJson) =
    List(
      _.hasLabel(json.label),
      _.hasAbbreviation(json.abbreviation)
    )

  override protected def validate(json: CampusJson) = None
}
