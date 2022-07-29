package controllers

import json.CourseFormat
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
    with Controller[CourseJson, Course]
    with CourseFormat {
  override protected implicit val writes: Writes[Course] =
    courseWrites

  override protected implicit val reads: Reads[CourseJson] =
    courseJsonFmt
}
