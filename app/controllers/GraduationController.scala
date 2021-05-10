package controllers

import models.{Graduation, GraduationJson}
import play.api.libs.json.{Reads, Writes}
import play.api.mvc.{AbstractController, ControllerComponents}
import service.GraduationService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class GraduationController @Inject() (
    cc: ControllerComponents,
    val service: GraduationService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Controller[GraduationJson, Graduation] {
  override protected implicit def writes: Writes[Graduation] = Graduation.format

  override protected implicit def reads: Reads[GraduationJson] =
    GraduationJson.format
}
