package controllers

import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext

import controllers.crud.Read
import models.Faculty
import play.api.libs.json.Writes
import play.api.mvc.AbstractController
import play.api.mvc.ControllerComponents
import service.FacultyService

@Singleton
final class FacultyController @Inject() (
    cc: ControllerComponents,
    val service: FacultyService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Read[String, Faculty] {
  implicit override def writes: Writes[Faculty] = Faculty.writes
}
