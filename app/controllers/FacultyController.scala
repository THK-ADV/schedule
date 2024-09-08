package controllers

import controllers.crud.Read
import models.Faculty
import play.api.libs.json.Writes
import play.api.mvc.{AbstractController, ControllerComponents}
import service.FacultyService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class FacultyController @Inject() (
    cc: ControllerComponents,
    val service: FacultyService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Read[String, Faculty] {
  override implicit def writes: Writes[Faculty] = Faculty.given_Format_Faculty
}
