package controllers

import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import service.SemesterPlanEntryService

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
final class SemesterPlanController @Inject() (
    cc: ControllerComponents,
    val service: SemesterPlanEntryService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc) {

  def all() =
    Action.async { implicit request =>
      parseFromToDate match {
        case Right((from, to)) =>
          service.repo
            .allFromView(from, to, preferredLanguage)
            .map(xs => Ok(Json.toJson(xs)))
        case Left(err) =>
          Future.successful(BadRequest(Json.obj("message" -> err)))
      }
    }
}
