package controllers

import json.FacultyFormat
import models.{Faculty, FacultyJson}
import play.api.libs.json.{Reads, Writes}
import play.api.mvc.{AbstractController, ControllerComponents}
import service.FacultyService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class FacultyController @Inject() (
    cc: ControllerComponents,
    val service: FacultyService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Controller[FacultyJson, Faculty]
    with FacultyFormat {
  override protected implicit val writes: Writes[Faculty] =
    facultyFmt

  override protected implicit val reads: Reads[FacultyJson] =
    facultyJsonFmt
}
