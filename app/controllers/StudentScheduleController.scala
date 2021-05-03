package controllers

import models.{StudentSchedule, StudentScheduleJson}
import play.api.libs.json.{Reads, Writes}
import play.api.mvc.{AbstractController, ControllerComponents}
import service.StudentScheduleService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class StudentScheduleController @Inject() (
    cc: ControllerComponents,
    val service: StudentScheduleService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Controller[StudentScheduleJson, StudentSchedule] {
  override protected implicit def writes: Writes[StudentSchedule] =
    StudentSchedule.format

  override protected implicit def reads: Reads[models.StudentScheduleJson] =
    StudentScheduleJson.format
}
