package controllers

import play.api.libs.json.{JsObject, Json, Writes}
import play.api.mvc.Result
import play.api.mvc.Results.{BadRequest, Created, Ok}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

trait JsonHttpResponse {
  implicit val ctx: ExecutionContext

  def okSeq[A](
      f: Future[Seq[A]]
  )(implicit writes: Writes[A]): Future[Result] =
    f.map(s => Ok(Json.toJson(s))).recover { case NonFatal(t) =>
      badRequest(t)
    }

  def ok[A](
      f: Future[A]
  )(implicit writes: Writes[A]): Future[Result] =
    f.map(s => Ok(Json.toJson(s))).recover { case NonFatal(t) =>
      badRequest(t)
    }

  def created[A](
      f: Future[A]
  )(implicit writes: Writes[A]): Future[Result] =
    f.map(s => Created(Json.toJson(s))).recover { case NonFatal(t) =>
      badRequest(t)
    }

  def err(t: Throwable): JsObject = Json.obj("msg" -> t.getMessage)

  def badRequest(t: Throwable): Result = BadRequest(err(t))
}
