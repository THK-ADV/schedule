package controllers

import json.TeachingUnitFormat
import models.{TeachingUnit, TeachingUnitJson}
import play.api.libs.json.{Reads, Writes}
import play.api.mvc.{AbstractController, ControllerComponents}
import service.TeachingUnitService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class TeachingUnitController @Inject() (
    cc: ControllerComponents,
    val service: TeachingUnitService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Controller[TeachingUnitJson, TeachingUnit]
    with TeachingUnitFormat {
  override protected implicit def writes: Writes[TeachingUnit] =
    teachingUnitFmt

  override protected implicit def reads: Reads[TeachingUnitJson] =
    teachingUnitJsonFmt
}
