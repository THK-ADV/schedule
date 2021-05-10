/*
package controllers

import models.{Semester, SemesterJson}
import play.api.libs.json.{Reads, Writes}
import play.api.mvc.{AbstractController, ControllerComponents}
import service.SemesterService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class SemesterController @Inject() (
    cc: ControllerComponents,
    val service: SemesterService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Controller[SemesterJson, Semester] {
  override protected implicit def writes: Writes[Semester] = Semester.format

  override protected implicit def reads: Reads[SemesterJson] =
    SemesterJson.format
}
*/
