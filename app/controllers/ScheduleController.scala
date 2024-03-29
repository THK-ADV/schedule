package controllers

import json.ScheduleFormat
import models.{Schedule, ScheduleJson}
import play.api.libs.json.{Reads, Writes}
import play.api.mvc.{AbstractController, ControllerComponents}
import service.ScheduleService

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class ScheduleController @Inject() (
    cc: ControllerComponents,
    val service: ScheduleService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Controller[ScheduleJson, Schedule]
    with RequestOps
    with ScheduleFormat {
  override protected implicit val writes: Writes[Schedule] =
    scheduleWrites

  override protected implicit val reads: Reads[models.ScheduleJson] =
    scheduleJsonFmt

  type CourseIds = List[UUID]

  def search() =
    Action(parse.json[CourseIds]) async { request =>
      val newRequest = request
        .appending("courses" -> request.body.map(_.toString))
        .eraseToAnyContent()
      all()(newRequest)
    }
}
