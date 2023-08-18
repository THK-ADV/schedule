package controllers

import controllers.crud.{JsonHttpResponse, Read}
import json.LanguageWrites
import models.Language
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
    with Read[String, Language]
    with LanguageWrites
    with JsonHttpResponse[Language]
