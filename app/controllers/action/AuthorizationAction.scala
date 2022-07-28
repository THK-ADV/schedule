package controllers.action

import auth.OAuthAuthorization
import provider.UserToken
import filename.FilenameOps
import play.api.libs.json.Json
import play.api.mvc.Results.Unauthorized
import play.api.mvc._

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

@Singleton
case class AuthorizationAction @Inject() (
    auth: OAuthAuthorization[UserToken],
    parser: BodyParsers.Default,
    implicit val executionContext: ExecutionContext
) extends ActionBuilder[UserTokenRequest, AnyContent] {

  override def invokeBlock[A](
      request: Request[A],
      block: UserTokenRequest[A] => Future[Result]
  ): Future[Result] =
    auth
      .authorized(request.headers.get(OAuthAuthorization.AuthorizationHeader))
      .flatMap(t => block(UserTokenRequest(request, t)))
      .recover { case NonFatal(e) =>
        Unauthorized(
          Json.obj(
            "message" -> e.getMessage,
            "stackTrace" -> e.getStackTrace.mkString("\n"),
            "source" -> s"${FilenameOps.fileName}:${FilenameOps.functionName}"
          )
        )
      }
}
