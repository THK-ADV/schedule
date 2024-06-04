package controllers

import database.repos.ScheduleEntryViewRepository
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
final class ScheduleEntryController @Inject() (
    cc: ControllerComponents,
    val viewRepo: ScheduleEntryViewRepository,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc) {

  def all() =
    Action.async { implicit request =>
      val extend = isExtended
      val lang = preferredLanguage
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
