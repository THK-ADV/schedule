package service

import database.repos.FacultyRepository
import database.tables.{FacultyDbEntry, FacultyTable}
import models.{Faculty, FacultyJson}
import service.abstracts.Service

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class FacultyService @Inject() (val repo: FacultyRepository)
    extends Service[FacultyJson, Faculty, FacultyDbEntry, FacultyTable] {

  override protected def toUniqueDbEntry(json: FacultyJson, id: Option[UUID]) =
    FacultyDbEntry(
      json.label,
      json.abbreviation,
      now(),
      id getOrElse UUID.randomUUID
    )

  override protected def canUpdate(
      json: FacultyJson,
      existing: FacultyDbEntry
  ): Boolean =
    json.label == existing.label

  override protected def uniqueCols(json: FacultyJson, table: FacultyTable) =
    List(table.hasLabel(json.label))

  override protected def validate(json: FacultyJson) = None
}
