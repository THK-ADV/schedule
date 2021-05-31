package service

import database.SQLDateConverter
import database.repos.ExaminationRegulationRepository
import database.tables.{
  ExaminationRegulationDbEntry,
  ExaminationRegulationTable
}
import models.ExaminationRegulation.ExaminationRegulationAtom
import models.{ExaminationRegulation, ExaminationRegulationJson}
import service.abstracts.Service

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class ExaminationRegulationService @Inject() (
    val repo: ExaminationRegulationRepository,
    implicit val ctx: ExecutionContext
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
      json.number,
      json.start,
      json.end.map(toSQLDate),
      now(),
      id getOrElse UUID.randomUUID
    )

  override protected def canUpdate(
      json: ExaminationRegulationJson,
      existing: ExaminationRegulationDbEntry
  ): Boolean =
    json.studyProgram == existing.studyProgram &&
      json.number == existing.number

  override protected def uniqueCols(
      json: ExaminationRegulationJson
  ) = List(
    _.studyProgram(json.studyProgram),
    _.hasNumber(json.number)
  )

  override protected def validate(json: ExaminationRegulationJson) =
    for {
      end <- json.end
      res <- Option.unless(json.start.isBefore(end))(
        new Throwable(s"invalid date boundaries")
      )
    } yield res

  def allAtoms(filter: Map[String, Seq[String]]) =
    all(filter, atomic = true).map(
      _.map(_.asInstanceOf[ExaminationRegulationAtom])
    )
}
