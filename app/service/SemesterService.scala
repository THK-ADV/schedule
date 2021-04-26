package service

import database.SQLDateConverter
import database.repos.SemesterRepository
import database.tables.SemesterTable
import models.{Semester, SemesterJson}
import service.abstracts.Service

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class SemesterService @Inject() (val repo: SemesterRepository)
    extends SQLDateConverter
    with Service[SemesterJson, Semester, SemesterTable] {

  override protected def toModel(json: SemesterJson, id: Option[UUID]) =
    Semester(
      json.label,
      json.abbreviation,
      json.start,
      json.end,
      json.examStart,
      id getOrElse UUID.randomUUID
    )

  override protected def canUpdate(
      json: SemesterJson,
      existing: Semester
  ): Boolean =
    existing.start == json.start && existing.end == json.end

  override protected def uniqueCols(json: SemesterJson, table: SemesterTable) =
    List(table.onStart(json.start), table.onEnd(json.end))

  override protected def validate(json: SemesterJson) = Option.when(
    json.end.isBefore(json.start)
  )(
    new Throwable(
      s"semester start should be before semester end, but was ${json.start} - ${json.end}"
    )
  )
}
