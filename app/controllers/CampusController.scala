package controllers

import json.CampusFormat
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
    with Controller[CampusJson, Campus]
    with CampusFormat {
  override protected implicit def writes: Writes[Campus] =
    campusFmt

  override protected implicit def reads: Reads[CampusJson] =
    campusJsonFmt
}
