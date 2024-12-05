package controllers

import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import database.repos.ScheduleEntryViewRepository
import play.api.cache.Cached
import play.api.libs.json.Json
import play.api.mvc.AbstractController
import play.api.mvc.ControllerComponents

@Singleton
final class ScheduleEntryController @Inject() (
    cc: ControllerComponents,
    viewRepo: ScheduleEntryViewRepository,
    cached: Cached,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc) {

  def all() = cached.status(_.toString, 200, 3600) {
    Action.async { implicit request =>
      val extend = isExtended
      val lang   = preferredLanguage
      if (!extend) {
        Future.successful(NoContent)
      } else {
        parseFromToDate match {
          case Right((from, to)) =>
            viewRepo.all(from, to, lang).map(xs => Ok(Json.toJson(xs)))
          case Left(err) =>
            Future.successful(BadRequest(Json.obj("message" -> err)))
        }
      }
    }
  }
}
