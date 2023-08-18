package controllers

import controllers.crud.{JsonHttpResponse, Read}
import json.GradeWrites
import models.Grade
import play.api.mvc.{AbstractController, ControllerComponents}
import service.GradeService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class GradeController @Inject() (
    cc: ControllerComponents,
    val service: GradeService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Read[String, Grade]
    with GradeWrites
    with JsonHttpResponse[Grade]
