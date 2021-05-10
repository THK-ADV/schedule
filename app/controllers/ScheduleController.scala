/*
package controllers

import models.{Schedule, ScheduleJson}
import play.api.libs.json.{Reads, Writes}
import play.api.mvc.{AbstractController, ControllerComponents}
import service.ScheduleService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class ScheduleController @Inject() (
    cc: ControllerComponents,
    val service: ScheduleService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Controller[ScheduleJson, Schedule] {
  override protected implicit def writes: Writes[Schedule] = Schedule.format

  override protected implicit def reads: Reads[models.ScheduleJson] =
    ScheduleJson.format
}
*/
