package controllers

import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext

import controllers.crud.Read
import models.Degree
import play.api.libs.json.Writes
import play.api.mvc.AbstractController
import play.api.mvc.ControllerComponents
import service.DegreeService

@Singleton
final class DegreeController @Inject() (
    cc: ControllerComponents,
    val service: DegreeService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Read[String, Degree] {
  implicit override def writes: Writes[Degree] = Degree.writes
}
