package controllers

import controllers.crud.{JsonHttpResponse, Read}
import json.FacultyWrites
import models.Faculty
import play.api.mvc.{AbstractController, ControllerComponents}
import service.FacultyService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
final class FacultyController @Inject() (
    cc: ControllerComponents,
    val service: FacultyService,
    implicit val ctx: ExecutionContext
) extends AbstractController(cc)
    with Read[String, Faculty]
    with FacultyWrites
    with JsonHttpResponse[Faculty]
