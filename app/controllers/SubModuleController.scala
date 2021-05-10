/*
package controllers

import models.{SubModule, SubModuleJson}
import play.api.libs.json.{Reads, Writes}
import play.api.mvc.{AbstractController, ControllerComponents}
import service.SubModuleService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class SubModuleController @Inject() (
    cc: ControllerComponents,
    val service: SubModuleService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Controller[SubModuleJson, SubModule] {
  override protected implicit def writes: Writes[SubModule] = SubModule.format

  override protected implicit def reads: Reads[SubModuleJson] =
    SubModuleJson.format
}
*/
