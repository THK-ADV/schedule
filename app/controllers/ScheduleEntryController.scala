package controllers

import database.repos.ScheduleEntryViewRepository
import ops.DateOps
import org.joda.time.LocalDateTime
import play.api.libs.json.Json
import play.api.mvc.{
  AbstractController,
  AnyContent,
  ControllerComponents,
  Request
}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Success, Try}

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
        (parseDate("from"), parseDate("to")) match {
          case (Some(Success(from)), Some(Success(to))) =>
            if (from.isBefore(to)) {
              viewRepo.all(from, to, lang).map(xs => Ok(Json.toJson(xs)))
            } else {
              val err =
                s"invalid date range: ${DateOps.print(from)} < ${DateOps.print(to)}"
              Future.successful(BadRequest(Json.obj("message" -> err)))
            }
          case (Some(Success(from)), None) =>
            viewRepo
              .all(from, from.plusMonths(1), lang)
              .map(xs => Ok(Json.toJson(xs)))
          case (None, Some(Success(to))) =>
            viewRepo
              .all(to.minusMonths(1), to, lang)
              .map(xs => Ok(Json.toJson(xs)))
          case (None, None) =>
            val now = LocalDateTime.now()
            viewRepo
              .all(now.minusMonths(1), now.plusMonths(1), lang)
              .map(xs => Ok(Json.toJson(xs)))
          case (from, to) =>
            Future.successful(
              BadRequest(
                Json.obj(
                  "message" -> "could not parse 'from' or 'to' parameter",
                  "from" -> from.toString,
                  "to" -> to.toString
                )
              )
            )
        }
      }
    }

  def parseDate(key: String)(implicit
      r: Request[AnyContent]
  ): Option[Try[LocalDateTime]] =
    r.getQueryString(key).map(a => Try(DateOps.parseDateTime(a)))
}
