package controllers

import database.tables.FacultyTable
import models.{Faculty, FacultyJson}
import play.api.libs.json.{Reads, Writes}
import play.api.mvc.{AbstractController, ControllerComponents}
import service.FacultyService

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class FacultyController @Inject() (
    cc: ControllerComponents,
    val service: FacultyService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Controller[FacultyJson, Faculty, FacultyTable] {
  override protected implicit def writes: Writes[Faculty] = Faculty.format

  override protected implicit def reads: Reads[FacultyJson] =
    FacultyJson.format
}
