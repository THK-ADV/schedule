import javax.inject.Singleton

import scala.annotation.unused
import scala.concurrent._

import play.api.http.HttpErrorHandler
import play.api.libs.json.Json
import play.api.mvc._
import play.api.mvc.Results._

@unused
@Singleton
class ErrorHandler extends HttpErrorHandler {

  def onClientError(
      request: RequestHeader,
      statusCode: Int,
      message: String
  ): Future[Result] =
    Future.successful(
      Status(statusCode)(
        Json.obj(
          "type"    -> "client error",
          "request" -> request.toString(),
          "message" -> message
        )
      )
    )

  def onServerError(
      request: RequestHeader,
      exception: Throwable
  ): Future[Result] =
    Future.successful(
      InternalServerError(
        Json.obj(
          "type"    -> "server error",
          "request" -> request.toString(),
          "message" -> exception.getMessage
        )
      )
    )
}
