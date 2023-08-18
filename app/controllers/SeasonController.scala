package controllers

import controllers.crud.{JsonHttpResponse, Read}
import json.SeasonWrites
import models.Season
import play.api.mvc.{AbstractController, ControllerComponents}
import service.SeasonService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class SeasonController @Inject() (
    cc: ControllerComponents,
    val service: SeasonService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Read[String, Season]
    with SeasonWrites
    with JsonHttpResponse[Season]
