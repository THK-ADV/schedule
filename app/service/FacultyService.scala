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
      json.number,
      now(),
      id getOrElse UUID.randomUUID
    )

  override protected def canUpdate(
      json: FacultyJson,
      existing: FacultyDbEntry
  ): Boolean =
    json.label == existing.label && json.number == existing.number

  override protected def uniqueCols(json: FacultyJson) =
    List(_.hasLabel(json.label), _.hasNumber(json.number))

  override protected def validate(json: FacultyJson) = None
}
