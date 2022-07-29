package controllers

import json.StudentScheduleFormat
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
    with Controller[StudentScheduleJson, StudentSchedule]
    with StudentScheduleFormat {
  override protected implicit val writes: Writes[StudentSchedule] =
    studentScheduleWrites

  override protected implicit val reads: Reads[models.StudentScheduleJson] =
    studentScheduleJsonFmt
}
