package controllers

import controllers.crud.Read
import models.Identity
import play.api.libs.json.Writes
import play.api.mvc.{AbstractController, ControllerComponents}
import service.PersonService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class PersonController @Inject() (
    cc: ControllerComponents,
    val service: PersonService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Read[String, Identity] {
  override implicit def writes: Writes[Identity] = Identity.writes
}
