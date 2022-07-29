package controllers

import json.UserFormat
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
    with Controller[UserJson, User]
    with UserFormat {
  override protected implicit val writes: Writes[User] =
    userFmt

  override protected implicit val reads: Reads[UserJson] =
    userJsonFmt
}
