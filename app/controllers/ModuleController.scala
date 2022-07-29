package controllers

import json.ModuleFormat
import models.{Module, ModuleJson}
import play.api.libs.json.{Reads, Writes}
import play.api.mvc.{AbstractController, ControllerComponents}
import service.ModuleService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class ModuleController @Inject() (
    cc: ControllerComponents,
    val service: ModuleService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Controller[ModuleJson, Module]
    with ModuleFormat.All {
  override protected implicit def writes: Writes[Module] =
    moduleWrites

  override protected implicit def reads: Reads[ModuleJson] =
    moduleJsonFmt
}
