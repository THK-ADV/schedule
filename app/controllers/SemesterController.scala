package controllers

import database.tables.SemesterTable
import models.{Semester, SemesterJson}
import play.api.libs.json.{Reads, Writes}
import play.api.mvc.{AbstractController, ControllerComponents}
import service.SemesterService

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class SemesterController @Inject() (
    cc: ControllerComponents,
    val service: SemesterService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Controller[SemesterJson, Semester, SemesterTable] {
  override protected implicit def writes: Writes[Semester] = Semester.format

  override protected implicit def reads: Reads[SemesterJson] =
    SemesterJson.format
}
