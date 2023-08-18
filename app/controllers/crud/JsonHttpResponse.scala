package controllers.crud

import play.api.libs.json.{JsObject, Json, Writes}
import play.api.mvc.Result
import play.api.mvc.Results.{BadRequest, Created, NotFound, Ok}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

trait JsonHttpResponse[A] {
  protected implicit val ctx: ExecutionContext

  protected implicit def writes: Writes[A]

  private def recover400(f: Future[Result]): Future[Result] =
    f.recover { case NonFatal(t) => badRequest(t) }

  def okSeq(f: Future[Seq[A]]): Future[Result] =
    recover400(f.map(s => Ok(Json.toJson(s))))

  def ok(f: Future[A]): Future[Result] =
    recover400(f.map(s => Ok(Json.toJson(s))))

  def okOpt(f: Future[Option[A]]): Future[Result] =
    recover400(
      f.map(s => s.fold(NotFound(Json.obj()))(s => Ok(Json.toJson(s))))
    )

  def created(f: Future[A]): Future[Result] =
    recover400(f.map(s => Created(Json.toJson(s))))

  def err(t: Throwable): JsObject = Json.obj("msg" -> t.getMessage)

  def badRequest(t: Throwable): Result = BadRequest(err(t))
}
