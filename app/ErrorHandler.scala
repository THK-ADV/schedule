import play.api.http.HttpErrorHandler
import play.api.libs.json.Json
import play.api.mvc.*
import play.api.mvc.Results.*

import java.io.{PrintWriter, StringWriter}
import javax.inject.Singleton
import scala.annotation.unused
import scala.concurrent.*

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
          "type" -> "client error",
          "request" -> request.toString(),
          "message" -> message
        )
      )
    )

  def onServerError(
      request: RequestHeader,
      exception: Throwable
  ): Future[Result] = {
    val writer = StringWriter()
    val printWriter = PrintWriter(writer)
    exception.printStackTrace(printWriter)

    Future.successful(
      InternalServerError(
        Json.obj(
          "type" -> "server error",
          "request" -> request.toString(),
          "message" -> exception.getMessage,
          "trace" -> writer.toString
        )
      )
    )
  }
}
