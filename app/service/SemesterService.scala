package service

import database.SQLDateConverter
import database.repos.SemesterRepository
import database.tables.{SemesterDbEntry, SemesterTable}
import models.{Semester, SemesterJson}
import service.abstracts.Service

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class SemesterService @Inject() (val repo: SemesterRepository)
    extends SQLDateConverter
    with Service[SemesterJson, Semester, SemesterDbEntry, SemesterTable] {

  override protected def toUniqueDbEntry(json: SemesterJson, id: Option[UUID]) =
    SemesterDbEntry(
      json.label,
      json.abbreviation,
      json.start,
      json.end,
      json.lectureStart,
      json.lectureEnd,
      now(),
      id getOrElse UUID.randomUUID
    )

  override protected def canUpdate(
      json: SemesterJson,
      existing: SemesterDbEntry
  ): Boolean =
    toLocalDate(existing.start) == json.start &&
      toLocalDate(existing.end) == json.end

  override protected def uniqueCols(json: SemesterJson) =
    List(_.onStart(json.start), _.onEnd(json.end))

  override protected def validate(json: SemesterJson) = Option.when(
    json.end.isBefore(json.start) && json.lectureEnd.isBefore(json.lectureStart)
  )(
    new Throwable(
      s"semester start should be before semester end, but was ${json.start} - ${json.end}"
    )
  )
}
