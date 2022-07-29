package controllers.action

import provider.UserToken
import play.api.mvc.{Request, WrappedRequest}

case class UserTokenRequest[A](unwrapped: Request[A], token: UserToken)
    extends WrappedRequest[A](unwrapped)
