package controllers

import models.{Campus, CampusJson}
import play.api.libs.json.{Reads, Writes}
import play.api.mvc.{AbstractController, ControllerComponents}
import service.CampusService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class CampusController @Inject() (
    cc: ControllerComponents,
    val service: CampusService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Controller[CampusJson, Campus] {
  override protected implicit def writes: Writes[Campus] = Campus.format

  override protected implicit def reads: Reads[models.CampusJson] =
    CampusJson.format
}
