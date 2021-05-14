package service

import database.SQLDateConverter
import database.repos.ExaminationRegulationRepository
import database.tables.{
  ExaminationRegulationDbEntry,
  ExaminationRegulationTable
}
import models.{ExaminationRegulation, ExaminationRegulationJson}
import service.abstracts.Service

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class ExaminationRegulationService @Inject() (
    val repo: ExaminationRegulationRepository
) extends SQLDateConverter
    with Service[
      ExaminationRegulationJson,
      ExaminationRegulation,
      ExaminationRegulationDbEntry,
      ExaminationRegulationTable
    ] {

  override protected def toUniqueDbEntry(
      json: ExaminationRegulationJson,
      id: Option[UUID]
  ) =
    ExaminationRegulationDbEntry(
      json.studyProgram,
      json.label,
      json.abbreviation,
      json.start,
      json.end,
      now(),
      id getOrElse UUID.randomUUID
    )

  override protected def canUpdate(
      json: ExaminationRegulationJson,
      existing: ExaminationRegulationDbEntry
  ): Boolean =
    json.studyProgram == existing.studyProgram &&
      json.abbreviation == existing.abbreviation

  override protected def uniqueCols(
      json: ExaminationRegulationJson
  ) = List(
    _.hasStudyProgram(json.studyProgram),
    _.hasAbbreviation(json.abbreviation)
  )

  override protected def validate(json: ExaminationRegulationJson) =
    Option.unless(json.start.isBefore(json.end))(
      new Throwable(s"invalid date boundaries")
    )
}
