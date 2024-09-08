package controllers

import controllers.crud.Read
import models.Degree
import play.api.libs.json.Writes
import play.api.mvc.{AbstractController, ControllerComponents}
import service.DegreeService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class DegreeController @Inject() (
    cc: ControllerComponents,
    val service: DegreeService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Read[String, Degree] {
  override implicit def writes: Writes[Degree] = Degree.given_Format_Degree
}
