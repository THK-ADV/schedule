package controllers

import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import play.api.cache.Cached
import play.api.libs.json.Json
import play.api.mvc.AbstractController
import play.api.mvc.ControllerComponents
import service.SemesterPlanEntryService

@Singleton
final class SemesterPlanController @Inject() (
    cc: ControllerComponents,
    service: SemesterPlanEntryService,
    cached: Cached,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc) {

  def all() = cached.status(_.toString, 200, 3600) {
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
}
