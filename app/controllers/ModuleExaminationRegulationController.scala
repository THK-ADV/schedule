package controllers

import json.ModuleExaminationRegulationFormat
import models.{ModuleExaminationRegulation, ModuleExaminationRegulationJson}
import play.api.libs.json.{Reads, Writes}
import play.api.mvc.{AbstractController, ControllerComponents}
import service.ModuleExaminationRegulationService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class ModuleExaminationRegulationController @Inject() (
    cc: ControllerComponents,
    val service: ModuleExaminationRegulationService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Controller[
      ModuleExaminationRegulationJson,
      ModuleExaminationRegulation
    ]
    with ModuleExaminationRegulationFormat {
  override protected implicit val writes: Writes[ModuleExaminationRegulation] =
    moduleExamRegWrites

  override protected implicit val reads
      : Reads[ModuleExaminationRegulationJson] =
    moduleExamRegJsonFmt
}
