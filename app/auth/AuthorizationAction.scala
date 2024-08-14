package auth

import play.api.libs.json.Json
import play.api.mvc.*
import play.api.mvc.Results.Unauthorized

import java.io.{PrintWriter, StringWriter}
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
case class AuthorizationAction @Inject() (
    auth: Authorization[UserToken],
    parser: BodyParsers.Default
)(implicit val executionContext: ExecutionContext)
    extends ActionBuilder[UserTokenRequest, AnyContent] {

  override def invokeBlock[A](
      request: Request[A],
      block: UserTokenRequest[A] => Future[Result]
  ): Future[Result] = {
    auth.authorize(
      request.headers.get(Authorization.AuthorizationHeader)
    ) match {
      case Success(token) => block(UserTokenRequest(request, token))
      case Failure(e) =>
        val writer = StringWriter()
        val printWriter = PrintWriter(writer)
        e.printStackTrace(printWriter)

        Future.successful(
          Unauthorized(
            Json.obj(
              "request" -> request.toString(),
              "message" -> e.getMessage,
              "stackTrace" -> writer.toString
            )
          )
        )
    }
  }
}
