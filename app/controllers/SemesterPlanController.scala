package controllers

import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import service.SemesterPlanEntryService

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Success

@Singleton
final class SemesterPlanController @Inject() (
    cc: ControllerComponents,
    val service: SemesterPlanEntryService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc) {

  def all() =
    Action.async { implicit request =>
      parseDate("today") match {
        case Some(Success(today)) =>
          service.repo
            .allFromView(today, preferredLanguage)
            .map(Ok(_))
        case _ =>
          Future.successful(
            BadRequest(
              Json.obj(
                "message" -> "needs 'today' query parameter to indicate semester bounds"
              )
            )
          )
      }
    }
}
