package controllers

import models.{ScheduleWIP, ScheduleWIPJson}
import play.api.libs.json.{Reads, Writes}
import play.api.mvc.{AbstractController, ControllerComponents}
import service.ScheduleWIPService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class ScheduleWIPController @Inject() (
    cc: ControllerComponents,
    val service: ScheduleWIPService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Controller[ScheduleWIPJson, ScheduleWIP] {
  override protected implicit def writes: Writes[ScheduleWIP] =
    ScheduleWIP.writes

  override protected implicit def reads: Reads[ScheduleWIPJson] =
    ScheduleWIPJson.format
}
