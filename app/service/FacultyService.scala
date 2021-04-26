package service

import database.repos.FacultyRepository
import database.tables.FacultyTable
import models.{Faculty, FacultyJson}
import service.abstracts.Service

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class FacultyService @Inject() (val repo: FacultyRepository)
    extends Service[FacultyJson, Faculty, FacultyTable] {

  override protected def toModel(json: FacultyJson, id: Option[UUID]) =
    Faculty(json.label, json.abbreviation, id getOrElse UUID.randomUUID)

  override protected def canUpdate(
      json: FacultyJson,
      existing: Faculty
  ): Boolean =
    json.label == existing.label

  override protected def uniqueCols(json: FacultyJson, table: FacultyTable) =
    List(table.hasLabel(json.label))
}
