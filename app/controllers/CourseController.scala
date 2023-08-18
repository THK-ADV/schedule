package controllers

import controllers.CourseController.CourseJson
import controllers.crud.{Create, JsonHttpResponse, Read}
import json.CourseWrites
import models.{Course, ModulePart}
import play.api.libs.json.{Json, Reads}
import play.api.mvc.{AbstractController, ControllerComponents}
import service.CourseService

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class CourseController @Inject() (
    cc: ControllerComponents,
    val service: CourseService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Read[UUID, Course]
    with Create[Course, CourseJson]
    with CourseWrites
    with JsonHttpResponse[Course] {

  implicit val modulePartReads: Reads[ModulePart] =
    Reads.of[String].map(ModulePart.apply)

  override implicit val reads: Reads[CourseJson] = Json.reads

  override def toModel(json: CourseJson): Course =
    Course(
      UUID.randomUUID(),
      json.semester,
      json.module,
      json.studyProgram,
      json.part
    )
}

object CourseController {
  case class CourseJson(
      semester: UUID,
      module: UUID,
      studyProgram: String,
      part: ModulePart
  )
}
