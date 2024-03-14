package controllers

import controllers.CourseController.CourseJson
import controllers.crud.{Create, Read}
import models.{Course, ModulePart}
import play.api.libs.json.{Json, Reads, Writes}
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
    with Create[Course, CourseJson] {

  implicit val modulePartReads: Reads[ModulePart] =
    Reads.of[String].map(ModulePart.apply)

  override implicit val reads: Reads[CourseJson] = Json.reads

  override def toModel(json: CourseJson): Course =
    Course(
      UUID.randomUUID(),
      json.semester,
      json.module,
      json.part
    )

  override implicit def writes: Writes[Course] = Course.writes
}

object CourseController {
  case class CourseJson(
      semester: UUID,
      module: UUID,
      part: ModulePart
  )
}
