package controllers

import json.ExaminationRegulationFormat
import models.{ExaminationRegulation, ExaminationRegulationJson}
import play.api.libs.json.{Reads, Writes}
import play.api.mvc.{AbstractController, ControllerComponents}
import service.ExaminationRegulationService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class ExaminationRegulationController @Inject() (
    cc: ControllerComponents,
    val service: ExaminationRegulationService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Controller[ExaminationRegulationJson, ExaminationRegulation]
    with ExaminationRegulationFormat.All {
  override protected implicit def writes: Writes[ExaminationRegulation] =
    examRegWrites

  override protected implicit def reads: Reads[ExaminationRegulationJson] =
    examRegJsonFmt
}
