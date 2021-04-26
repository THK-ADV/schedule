package service

import database.SQLDateConverter
import database.repos.ExaminationRegulationRepository
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

  override protected def toModel(
      json: ExaminationRegulationJson,
      id: Option[UUID]
  ) =
    ExaminationRegulation(
      json.studyProgram,
      json.label,
      json.abbreviation,
      json.accreditation,
      json.activation,
      json.expiring,
      id getOrElse UUID.randomUUID
    )

  override protected def canUpdate(
      json: ExaminationRegulationJson,
      existing: ExaminationRegulation
  ): Boolean =
    json.studyProgram == existing.studyProgram &&
      json.accreditation == existing.accreditation &&
      json.activation == existing.activation &&
      json.expiring == existing.expiring

  override protected def uniqueCols(
      json: ExaminationRegulationJson,
      table: ExaminationRegulationTable
  ) = List(
    table.isUniqueTo(
      json.studyProgram,
      json.accreditation,
      json.activation,
      json.expiring
    )
  )

  override protected def validate(json: ExaminationRegulationJson) =
    Option.unless(
      json.accreditation.isBefore(json.expiring) &&
        json.activation.isAfter(json.accreditation) // TODO is this correct?
    )(new Throwable(s"invalid date boundaries"))
}
