package controllers

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
    with Controller[ModuleJson, Module] {
  override protected implicit def writes: Writes[Module] = Module.writes

  override protected implicit def reads: Reads[ModuleJson] = ModuleJson.format
}
