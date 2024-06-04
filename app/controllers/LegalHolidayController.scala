package controllers

import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import service.LegalHolidayService

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
final class LegalHolidayController @Inject() (
    cc: ControllerComponents,
    val service: LegalHolidayService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc) {

  def all() =
    Action.async { implicit request =>
      parseFromToDate match {
        case Right((from, to)) =>
          service.all(from, to).map(xs => Ok(Json.toJson(xs)))
        case Left(err) =>
          Future.successful(BadRequest(Json.obj("message" -> err)))
      }
    }
}
