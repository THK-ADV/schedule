/*
package service

import database.SQLDateConverter
import database.tables.ExaminationRegulationTable
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
        ExaminationRegulationTable
      ] {

    override protected def toUniqueDbEntry(
        json: ExaminationRegulationJson,
        id: Option[UUID]
    ) =
      ExaminationRegulation(
        json.studyProgram,
        json.label,
        json.abbreviation,
        json.start,
        json.end,
        id getOrElse UUID.randomUUID
      )

    override protected def canUpdate(
        json: ExaminationRegulationJson,
        existing: ExaminationRegulation
    ): Boolean =
      json.studyProgram == existing.studyProgram &&
        json.abbreviation == existing.abbreviation

    override protected def uniqueCols(
        json: ExaminationRegulationJson,
        table: ExaminationRegulationTable
    ) = List(
      table.hasStudyProgram(json.studyProgram),
      table.hasAbbreviation(json.abbreviation)
    )

    override protected def validate(json: ExaminationRegulationJson) =
      Option.unless(json.start.isBefore(json.end))(
        new Throwable(s"invalid date boundaries")
      )
  }
*/
