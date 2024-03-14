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
    Action.async { r =>
      val extend = r
        .getQueryString("extend")
        .flatMap(_.toBooleanOption)
        .getOrElse(false)
      if (extend)
        viewRepo.all().map(xs => Ok(Json.toJson(xs)))
      else
        Future.successful(NoContent)
    }
}
