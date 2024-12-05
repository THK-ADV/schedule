package controllers

import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext

import controllers.crud.Create
import controllers.CourseController.CourseJson
import models.Course
import models.CourseId
import play.api.cache.Cached
import play.api.libs.json.Json
import play.api.libs.json.Reads
import play.api.libs.json.Writes
import play.api.mvc.AbstractController
import play.api.mvc.ControllerComponents
import service.CourseService

@Singleton
final class CourseController @Inject() (
    cc: ControllerComponents,
    val service: CourseService,
    cached: Cached,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Create[UUID, Course, CourseJson] {

  implicit val modulePartReads: Reads[CourseId] =
    Reads.of[String].map(CourseId.apply)

  implicit override val reads: Reads[CourseJson] = Json.reads

  override def toModel(json: CourseJson): Course =
    Course(
      UUID.randomUUID(),
      json.semester,
      json.module,
      json.part
    )

  implicit override def writes: Writes[Course] = Course.writes

  def all() = cached.status(_.toString, 200, 3600)(
    Action.async(_ => service.all().map(xs => Ok(Json.toJson(xs))))
  )
}

object CourseController {
  case class CourseJson(
      semester: UUID,
      module: UUID,
      part: CourseId
  )
}
