package controllers

import models.{Schedule, ScheduleJson}
import play.api.libs.json.{Json, Reads, Writes}
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
    with Controller[ScheduleJson, Schedule] {
  override protected implicit def writes: Writes[Schedule] = Schedule.writes

  override protected implicit def reads: Reads[models.ScheduleJson] =
    ScheduleJson.format

  case class CourseIds(courseIds: List[UUID])

  implicit val readsCourseIds: Reads[CourseIds] = Json.reads[CourseIds]

  def search() = Action(parse.json[CourseIds]).async { r =>
    okSeq(service.allByCourses(r.body.courseIds, parseAtomic(r.queryString)))
  }
}
