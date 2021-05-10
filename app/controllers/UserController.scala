package controllers

import models.{User, UserJson}
import play.api.libs.json.{Reads, Writes}
import play.api.mvc.{AbstractController, ControllerComponents}
import service.UserService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class UserController @Inject() (
    cc: ControllerComponents,
    val service: UserService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Controller[UserJson, User] {
  override protected implicit def writes: Writes[User] =
    User.formatUser

  override protected implicit def reads: Reads[UserJson] =
    UserJson.format
}
