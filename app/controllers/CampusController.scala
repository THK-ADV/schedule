package controllers

import controllers.crud.{JsonHttpResponse, Read}
import json.CampusWrites
import models.Campus
import play.api.mvc.{AbstractController, ControllerComponents}
import service.CampusService

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class CampusController @Inject() (
    cc: ControllerComponents,
    val service: CampusService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Read[UUID, Campus]
    with CampusWrites
    with JsonHttpResponse[Campus]
