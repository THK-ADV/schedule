package controllers

import controllers.crud.Read
import models.Language
import play.api.libs.json.Writes
import play.api.mvc.{AbstractController, ControllerComponents}
import service.LanguageService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class LanguageController @Inject() (
    cc: ControllerComponents,
    val service: LanguageService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Read[String, Language] {
  override implicit def writes: Writes[Language] = Language.writes
}
