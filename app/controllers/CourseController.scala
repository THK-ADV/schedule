package controllers

import models.{Course, CourseJson}
import play.api.libs.json.{Reads, Writes}
import play.api.mvc.{AbstractController, ControllerComponents}
import service.CourseService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class CourseController @Inject() (
    cc: ControllerComponents,
    val service: CourseService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Controller[CourseJson, Course] {
  override protected implicit def writes: Writes[Course] =
    Course.format

  override protected implicit def reads: Reads[CourseJson] =
    CourseJson.format
}
